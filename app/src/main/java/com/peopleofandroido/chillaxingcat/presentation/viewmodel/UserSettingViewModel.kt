package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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

    private val _notificationEnabled: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val notificationEnabled: NotNullMutableLiveData<Boolean>
        get() = _notificationEnabled
    private var mCalender: GregorianCalendar? = null
    var isInitial: Boolean = false

    private var alarmManager: AlarmManager?

    init {
        mCalender = GregorianCalendar()
        alarmManager =
            getApplication<Application>().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        loadNotificationStatus()
        loadReminderTime()
        loadReminderText()
        loadGoalRestingTime()
    }

    private fun loadNotificationStatus() {
        viewModelScope.launch() {
            val result = useCases.getNotificationStatus()
            result.data?.let { it ->
                _notificationEnabled.value = it
            }
            logd("getNotificationStatus()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun loadReminderTime() {
        viewModelScope.launch() {
            val result = useCases.getReminderTime()
            result.data?.let { it ->
                if (it.isNotEmpty()) {
                    _reminderTime.value = it
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
                    _reminderText.value = it
                }
            }
            logd("getReminderText()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private fun loadGoalRestingTime() {
        viewModelScope.launch() {
            val result = useCases.getGoalRestingTime()
            result.data?.let { it ->
                val regex = Regex("[0-2][0-9][:][0-5][0-9]")
                if (regex.matchEntire(it)?.groupValues?.size?:0 > 0) {
                    val goalRestingTime = it.split(":")
                    if (goalRestingTime.size >= 2) {
                        _goalRestingTimeHour.value = goalRestingTime[0]
                        _goalRestingTimeMinute.value = goalRestingTime[1]
                    }
                }
            }
            logd("getGoalRestingTime()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    private suspend fun storeReminderText(text : String) {
        val result = useCases.putReminderText(text)
        result.data?.let {
            if (it) {
                logd("putReminderText(): success")
                _reminderText.value = text
            } else
                logd("putReminderText(): fail")
        } ?: run {
            logd("putReminderText(): error")
        }
    }

    private fun storeGoalRestingTime(hour: Int, minute: Int) {
        viewModelScope.launch() {
            val result = useCases.putGoalRestingTime(changeToDisplayTime(hour, minute))
            result.data?.let {
                if (it) {
                    logd("putGoalRestingTime(): success")
                    _goalRestingTimeHour.value = hour.toString()
                } else
                    logd("putGoalRestingTime(): fail")
            } ?: run {
                logd("putGoalRestingTime(): error")
            }
        }
    }

    private suspend fun storeReminderTime(time: String) {
        val result = useCases.putReminderTime(time)
        result.data?.let {
            if (it) {
                logd("putReminderTime(): success")
                _reminderTime.value = time
            } else
                logd("putReminderTime(): fail")
        } ?: run {
            logd("putReminderTime(): error")
        }
    }

    private suspend fun storeAndSetNotification(notificationEnabled : Boolean, reminderText: String) {
        val result = useCases.putNotificationStatus(notificationEnabled)
        result.data?.let {
            if (it) {
                logd("putNotificationStatus(): success")
                if (notificationEnabled) {
                    val time = _reminderTime.value.split(":")
                    setAlarm(Integer.parseInt(time[0]), Integer.parseInt(time[1]), reminderText)
                } else {
                    cancelAlarm()
                }
            } else
                logd("putNotificationStatus(): fail")

        } ?: run {
            logd("putNotificationStatus(): error")
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

    private fun setAlarm(hour: Int, minute: Int, reminderText: String) {
        if (!_notificationEnabled.value) return

        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(getApplication<Application>(), AlarmReceiver::class.java)
        receiverIntent.putExtra("reminder_text", reminderText)
        logd("ReminderText : $reminderText")
        val pendingIntent = getBroadcast(getApplication<Application>(), 0, receiverIntent, FLAG_MUTABLE)
        //alarm 등록 전, 이전 notification cancel
        alarmManager?.cancel(pendingIntent)

        // Set the alarm to start at time and minute
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (this.time < Date()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        logd("setAlarm: ${calendar.time}")

        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager?.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//        } else {
//            alarmManager?.setExact(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//        }
    }

    fun saveAll() {
        if ((_notificationEnabled.value &&
            _reminderText.value.isNotEmpty() &&
            checkGoalRestingTimeHour() &&
            checkGoalRestingTimeMinute()) ||
            (!_notificationEnabled.value && checkGoalRestingTimeHour() &&
                    checkGoalRestingTimeMinute())) {
            viewModelScope.launch {
                storeReminderTime(_reminderTime.value)
                storeGoalRestingTime(
                    Integer.parseInt(_goalRestingTimeHour.value),
                    Integer.parseInt(_goalRestingTimeMinute.value)
                )
                storeReminderText(_reminderText.value)
                storeAndSetNotification(_notificationEnabled.value, _reminderText.value)
                Toast.makeText(
                    getApplication(),
                    getApplication<Application>().getText(R.string.user_setting_toast_saving),
                    Toast.LENGTH_SHORT
                ).show()

                Toast.makeText(
                    getApplication(),
                    getApplication<Application>().getText(R.string.user_setting_toast_saved),
                    Toast.LENGTH_SHORT
                ).show()
                _actionEvent.value = Event(Action.DialogAction("pop"))
            }

        } else {
            Toast.makeText(getApplication(), getApplication<Application>().getText(R.string.user_setting_toast_input_required), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkGoalRestingTimeHour(): Boolean {
        return (_goalRestingTimeHour.value.isNotEmpty() &&
                _goalRestingTimeHour.value.toIntOrNull() ?: -1 >= 0 &&
                _goalRestingTimeHour.value.toIntOrNull() ?: Integer.MAX_VALUE <= 12)
    }

    private fun checkGoalRestingTimeMinute(): Boolean {
        return (_goalRestingTimeMinute.value.isNotEmpty() &&
                Integer.parseInt(_goalRestingTimeMinute.value) >= 0 &&
                Integer.parseInt(_goalRestingTimeMinute.value) <= 60)
    }

    fun changeToDisplayTime(hour: Int, minute: Int) : String {
        //hour 는 24 이하, minute는 60 이하인 범위 체크 해야할까
        return String.format("%02d:%02d", hour, minute)
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}