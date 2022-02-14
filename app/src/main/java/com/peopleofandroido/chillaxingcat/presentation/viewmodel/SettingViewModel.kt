package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.AlarmReceiver
import com.peopleofandroido.chillaxingcat.alarmManager
import com.peopleofandroido.chillaxingcat.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


class SettingViewModel(
    application: Application,
    private val navManager : NavManager
) : AndroidViewModel(application) {
    private val keyWorkStartTime = stringPreferencesKey("work_start_time")
    private val _workStartTime: NotNullMutableLiveData<String> = NotNullMutableLiveData("9:00")
    val workStartTime: NotNullMutableLiveData<String>
        get() = _workStartTime
    private val keyWorkFinishTime = stringPreferencesKey("work_finish_time")
    private val _workFinishTime: NotNullMutableLiveData<String> = NotNullMutableLiveData("18:00")
    val workFinishTime: NotNullMutableLiveData<String>
        get() = _workFinishTime

    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent
    private var mCalender: GregorianCalendar? = null

    init {
        mCalender = GregorianCalendar()

        loadWorkStartTime()
        loadWorkFinishTime()
    }

    fun showTimeSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("setting_time_setting_dialog"))
    }

    fun showJobSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("setting_job_setting_dialog"))
    }

    private fun loadWorkStartTime() {
        viewModelScope.launch {
            val data = getApplication<Application>().dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    preferences[keyWorkStartTime] ?: "9:00"
                }
            workStartTime.value = data.first()
            logd("Load workStartTime: ${workStartTime.value}")
        }
    }

    private fun loadWorkFinishTime() {
        viewModelScope.launch {
            val data = getApplication<Application>().dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }.map { preferences ->
                    preferences[keyWorkFinishTime] ?: "18:00"
                }
            workFinishTime.value = data.first()
            logd("Load workFinishTime: ${workFinishTime.value}")
        }
    }

    fun storeWorkStartTime(startTime : String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[keyWorkStartTime] = startTime
                withContext(Dispatchers.Main) {
                    workStartTime.value = startTime
                    logd("Stored workStartTime: " + workStartTime.value)
                }
            }
        }
    }

    fun storeWorkFinishTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            val finishTime: String = changeToDisplayTime(hour, minute)

            getApplication<Application>().dataStore.edit { preferences ->
                preferences[keyWorkFinishTime] = finishTime
                withContext(Dispatchers.Main) {
                    workFinishTime.value = finishTime
                    logd("Stored workFinishTime: " + workFinishTime.value)

                    setAlarm(hour, minute)
                }
            }
        }
    }

    private fun setAlarm(hour: Int, minute: Int) {
        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(getApplication<Application>(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(getApplication<Application>(), 0, receiverIntent, 0)

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

    fun changeToDisplayTime(hour: Int, minute: Int) : String {
        return String.format("%02d:%02d", hour, minute)
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}