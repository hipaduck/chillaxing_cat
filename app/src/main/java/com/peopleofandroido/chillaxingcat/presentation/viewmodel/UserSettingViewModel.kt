package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.AlarmReceiver
import com.peopleofandroido.chillaxingcat.alarmManager
import com.peopleofandroido.chillaxingcat.domain.UseCases
import kotlinx.coroutines.launch
import java.util.*

class UserSettingViewModel (
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<SettingViewModel.Action>> = NotNullMutableLiveData(
        Event(SettingViewModel.Action())
    )
    val actionEvent: NotNullMutableLiveData<Event<SettingViewModel.Action>>
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

    private val _pushAvailable: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val pushAvailable: NotNullMutableLiveData<Boolean>
        get() = _pushAvailable
    private var mCalender: GregorianCalendar? = null
    var isInitial: Boolean = false

    init {
        mCalender = GregorianCalendar()

        loadPushAvailable()
        loadReminderTime()
        loadReminderText()
        loadGoalRestingTimeHour()
        loadGoalRestingTimeMinute()
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

    private fun loadGoalRestingTimeHour() {
        viewModelScope.launch() {
            val result = useCases.getGoalRestingTimeHour()
            result.data?.let { it ->
                if (it != 0) {
                    goalRestingTimeHour.value = it.toString()
                }
            }
            logd("getGoalRestingTimeHour()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun loadGoalRestingTimeMinute() {
        viewModelScope.launch() {
            val result = useCases.goalRestingTimeMinute()
            result.data?.let { it ->
                if (it != 0) {
                    goalRestingTimeMinute.value = it.toString()
                }
            }
            logd("getGoalRestingTimeMinute()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    fun storeReminderText(text : String) {
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

    private fun storeGoalRestingTimeHour(hour : Int) {
        viewModelScope.launch() {
            val result = useCases.putGoalRestingTimeHour(hour)
            result.data?.let {
                if (it) {
                    logd("putGoalRestingTimeHour(): success")
                    goalRestingTimeHour.value = hour.toString()
                } else
                    logd("putGoalRestingTimeHour(): fail")
            } ?: run {
                logd("putGoalRestingTimeHour(): error")
            }
        }
    }

    private fun storeGoalRestingTimeMinute(minute : Int) {
        viewModelScope.launch() {
            val result = useCases.putGoalRestingTimeMinute(minute)
            result.data?.let {
                if (it) {
                    logd("putGoalRestingTimeMinute(): success")
                    goalRestingTimeMinute.value = minute.toString()
                } else
                    logd("putGoalRestingTimeMinute(): fail")
            } ?: run {
                logd("putGoalRestingTimeMinute(): error")
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
            } ?: run {
                logd("putNotificationStatus(): error")
            }
        }
    }

    fun showTimeDialog() {
        _actionEvent.value = Event(SettingViewModel.Action.DialogAction("show_time_dialog"))
    }

    private fun cancelAlarm() {
        val receiverIntent = Intent(getApplication<Application>(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(getApplication<Application>(), 0, receiverIntent, PendingIntent.FLAG_MUTABLE)
        alarmManager?.cancel(pendingIntent)
    }

    private fun setAlarm(hour: Int, minute: Int) {
        if (!pushAvailable.value) return

        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(getApplication<Application>(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(getApplication<Application>(), 0, receiverIntent, PendingIntent.FLAG_MUTABLE)

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
            goalRestingTimeHour.value.isNotEmpty() &&
            goalRestingTimeMinute.value.isNotEmpty()) ||
            (!pushAvailable.value && goalRestingTimeHour.value.isNotEmpty() &&
                    goalRestingTimeMinute.value.isNotEmpty())) {
            Toast.makeText(getApplication(), "저장이 완료되었습니다 :)", Toast.LENGTH_SHORT).show()
            storeReminderTime(reminderTime.value)
            storeGoalRestingTimeHour(Integer.parseInt(goalRestingTimeHour.value))
            storeGoalRestingTimeMinute(Integer.parseInt(goalRestingTimeMinute.value))
            storeReminderText(reminderText.value)
            storePushSetting(pushAvailable.value)

            if (isInitial) {
                _actionEvent.value = Event(SettingViewModel.Action.DialogAction("pop_to_main"))
            }
        } else {
            Toast.makeText(getApplication(), "설정 값을 입력해 주세요 :)", Toast.LENGTH_SHORT).show()
        }
    }

    fun changeToDisplayTime(hour: Int, minute: Int) : String {
        //hour 는 24 이하, minute는 60 이하인 범위 체크 해야할까
        return String.format("%02d:%02d", hour, minute)
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}