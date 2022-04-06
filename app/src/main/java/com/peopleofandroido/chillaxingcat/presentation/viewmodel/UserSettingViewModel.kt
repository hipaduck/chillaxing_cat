package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.AlarmReceiver
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.domain.UseCases
import kotlinx.coroutines.launch
import java.util.*

class UserSettingViewModel (
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(
        Event(Action())
    )
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    private val _reminderTime: NotNullMutableLiveData<String> = NotNullMutableLiveData("18:00")
    val reminderTime: NotNullMutableLiveData<String>
        get() = _reminderTime

    private val _reminderText: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val reminderText: NotNullMutableLiveData<String>
        get() = _reminderText

    private val _goalRestingTimeHour: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val goalRestingTimeHour: NotNullMutableLiveData<String>
        get() = _goalRestingTimeHour
    private val _goalRestingTimeMinute: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val goalRestingTimeMinute: NotNullMutableLiveData<String>
        get() = _goalRestingTimeMinute

    private val _pushEnable: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val pushAvailable: NotNullMutableLiveData<Boolean>
        get() = _pushEnable
    private var mCalender: GregorianCalendar? = null
    var isInitial: Boolean = false

    private var alarmManager: AlarmManager?

    init {
        mCalender = GregorianCalendar()
        alarmManager =
            getApplication<Application>().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        loadPushAvailable()
        loadReminderTime()
        loadReminderText()
        loadGoalRestingTime()
    }

    private fun loadPushAvailable() {
        viewModelScope.launch() {
            val result = useCases.getNotificationStatus()
            result.data?.let { it ->
                pushAvailable.value = it
            }
            logd("getNotificationStatus()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun loadReminderTime() {
        viewModelScope.launch() {
            val result = useCases.getReminderTime()
            result.data?.let { it ->
                if (it.isNotEmpty()) {
                    reminderTime.value = it
                }
            }
            logd("getReminderTime()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun loadReminderText() {
        viewModelScope.launch() {
            val result = useCases.getReminderText()
            result.data?.let { it ->
                if (it.isNotEmpty()) {
                    reminderText.value = it
                }
            }
            logd("getReminderText()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun loadGoalRestingTime() {
        viewModelScope.launch() {
            val result = useCases.getGoalRestingTime()
            result.data?.let { it ->
                if (it.isNotEmpty() && it.contains(":")) {
                    val goalRestingTime = it.split(":")
                    if (goalRestingTime.size >= 2) {
                        goalRestingTimeHour.value = goalRestingTime[0]
                        goalRestingTimeMinute.value = goalRestingTime[1]
                    }
                }
            }
            logd("getGoalRestingTime()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun storeReminderText(text : String) {
        viewModelScope.launch() {
            val result = useCases.putReminderText(text)
            result.data?.let {
                if (it) {
                    logd("putReminderText(): success")
                    reminderText.value = text
                } else
                    logd("putReminderText(): fail")
            } ?: run {
                logd("putReminderText(): error")
            }
        }
    }

    private fun storeGoalRestingTime(hour: Int, minute: Int) {
        viewModelScope.launch() {
            val result = useCases.putGoalRestingTime(changeToDisplayTime(hour, minute))
            result.data?.let {
                if (it) {
                    logd("putGoalRestingTime(): success")
                    goalRestingTimeHour.value = hour.toString()
                } else
                    logd("putGoalRestingTime(): fail")
            } ?: run {
                logd("putGoalRestingTime(): error")
            }
        }
    }

    private fun storeReminderTime(time: String) {
        viewModelScope.launch {
            val result = useCases.putReminderTime(time)
            result.data?.let {
                if (it) {
                    logd("putReminderTime(): success")
                    reminderTime.value = time
                } else
                    logd("putReminderTime(): fail")
            } ?: run {
                logd("putReminderTime(): error")
            }
        }
    }

    private fun storePushSetting(getPush : Boolean) {
        viewModelScope.launch() {
            val result = useCases.putNotificationStatus(getPush)
            result.data?.let {
                if (it) {
                    logd("putNotificationStatus(): success")
                    pushAvailable.value = getPush
                    if (getPush) {
                        setAlarm(Integer.parseInt(goalRestingTimeHour.value), Integer.parseInt(goalRestingTimeMinute.value))
                    } else {
                        cancelAlarm()
                    }
                } else
                    logd("putNotificationStatus(): fail")

                //저장이 끝나면 이동
                if (isInitial) {
                    _actionEvent.value = Event(Action.DialogAction("pop"))
                }
            } ?: run {
                logd("putNotificationStatus(): error")
            }
        }
    }

    fun showTimeDialog() {
        _actionEvent.value = Event(Action.DialogAction("show_time_dialog"))
    }

    private fun cancelAlarm() {
        val receiverIntent = Intent(getApplication<Application>(), AlarmReceiver::class.java)
        val pendingIntent = getBroadcast(getApplication<Application>(), 0, receiverIntent, FLAG_MUTABLE)
        alarmManager?.cancel(pendingIntent)
    }

    private fun setAlarm(hour: Int, minute: Int) {
        if (!pushAvailable.value) return

        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(getApplication<Application>(), AlarmReceiver::class.java)
        val pendingIntent = getBroadcast(getApplication<Application>(), 0, receiverIntent,
            FLAG_MUTABLE
        )

        //alarm 등록 전, 이전 push cancel
        alarmManager?.cancel(pendingIntent)

        // Set the alarm to start at time and minute
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.time < Date()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun saveAll() {
        if ((pushAvailable.value &&
            reminderText.value.isNotEmpty() &&
            checkGoalRestingTimeHour() &&
            checkGoalRestingTimeMinute()) ||
            (!pushAvailable.value && checkGoalRestingTimeHour() &&
                    checkGoalRestingTimeMinute())) {
            storeReminderTime(reminderTime.value)
            storeGoalRestingTime(Integer.parseInt(goalRestingTimeHour.value), Integer.parseInt(goalRestingTimeMinute.value))
            storeReminderText(reminderText.value)
            storePushSetting(pushAvailable.value)

            Toast.makeText(getApplication(), getApplication<Application>().getText(R.string.user_setting_toast_saved), Toast.LENGTH_SHORT).show()
            _actionEvent.value = Event(Action.DialogAction("pop"))
        } else {
            Toast.makeText(getApplication(), getApplication<Application>().getText(R.string.user_setting_toast_input_required), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkGoalRestingTimeHour(): Boolean {
        return (goalRestingTimeHour.value.isNotEmpty() &&
                Integer.parseInt(goalRestingTimeHour.value) >= 0 &&
                Integer.parseInt(goalRestingTimeHour.value) <= 12)
    }

    private fun checkGoalRestingTimeMinute(): Boolean {
        return (goalRestingTimeMinute.value.isNotEmpty() &&
                Integer.parseInt(goalRestingTimeMinute.value) >= 0 &&
                Integer.parseInt(goalRestingTimeMinute.value) <= 60)
    }

    fun changeToDisplayTime(hour: Int, minute: Int) : String {
        //hour 는 24 이하, minute는 60 이하인 범위 체크 해야할까
        return String.format("%02d:%02d", hour, minute)
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}