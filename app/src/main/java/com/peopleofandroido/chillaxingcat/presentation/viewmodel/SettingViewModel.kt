package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.AlarmReceiver
import com.peopleofandroido.chillaxingcat.dataStore
import com.peopleofandroido.chillaxingcat.domain.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SettingViewModel(
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
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
    private var alarmManager: AlarmManager? = null
    private var mCalender: GregorianCalendar? = null

    init {
        mCalender = GregorianCalendar()

        loadWorkStartTime()
        loadWorkFinishTime()
    }

    fun showTimeSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("time_setting_dialog"))
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

    fun storeWorkFinishTime(finishTime : String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[keyWorkFinishTime] = finishTime
                withContext(Dispatchers.Main) {
                    workFinishTime.value = finishTime
                    logd("Stored workFinishTime: " + workFinishTime.value)
                }
            }
        }
    }
    
    open class Action {
        class DialogAction(val type: String): Action()
    }
}