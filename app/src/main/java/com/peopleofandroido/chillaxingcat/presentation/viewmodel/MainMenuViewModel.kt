package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.domain.Status
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.base.util.loge
import com.peopleofandroido.chillaxingcat.AlarmReceiver
import com.peopleofandroido.chillaxingcat.alarmManager
import com.peopleofandroido.chillaxingcat.dataStore
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.model.YearMonth
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainMenuViewModel(
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(
        Event(Action())
    )
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    private val keyWorkStartTime = stringPreferencesKey("work_start_time")
    private val keyWorkFinishTime = stringPreferencesKey("work_finish_time")

    init {
        checkTimeSetting()
//        _actionEvent.value = Event(Action.DialogAction("main_time_setting_dialog")) //TODO : TEST
    }

    fun moveToSetting() {
        viewModelScope.launch {
            val navigationDirection = MainMenuFragmentDirections.actionMainMenuFragmentToSettingFragment()
            navManager.navigate(navigationDirection)
        }
    }

    fun moveToDueDate() {
        viewModelScope.launch {
            val navigationDirection = MainMenuFragmentDirections.actionMainMenuFragmentToDueDateFragment()
            navManager.navigate(navigationDirection)
        }
    }

    fun moveToCalendar() {
        viewModelScope.launch {
            val navigationDirection = MainMenuFragmentDirections.actionMainMenuFragmentToCalendarFragment()
            navManager.navigate(navigationDirection)
        }
    }

    fun test() {
        viewModelScope.launch {
            val yearMonth = YearMonth(2021, 9)
            val holidayResult = useCases.getHoliday(yearMonth)
            when (holidayResult.status) {
                Status.SUCCESS -> {
                    logd("holiday data: ${holidayResult.data}")
                    holidayResult.data?.let {
                        logd("holiday string data: $it")
                    }
                }
                Status.ERROR -> {
                    loge("holiday error: ${holidayResult.message}")
                }
            }
        }
    }

    private fun checkTimeSetting() {
            viewModelScope.launch {
                getApplication<Application>().dataStore.data
                    .catch {
                        logd("There is no Data")
                        _actionEvent.value = Event(Action.DialogAction("main_time_setting_dialog"))
                    }.map { preferences ->
                        if(preferences[keyWorkStartTime] != null) {
                            _actionEvent.value = Event(Action.DialogAction("main_time_setting_dialog"))
                        } else {
                            logd("There is Data")
                        }
                    }.onEmpty {
                        logd("There is no Data")
                        _actionEvent.value = Event(Action.DialogAction("main_time_setting_dialog"))
                    }
        }
    }

    fun storeWorkStartTime(startTime : String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[keyWorkStartTime] = startTime
                withContext(Dispatchers.Main) {
                    logd("Initialized workStartTime: $startTime")
                }
            }
        }
    }

    fun storeWorkFinishTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            val finishTime: String = String.format("%02d:%02d", hour, minute)

            getApplication<Application>().dataStore.edit { preferences ->
                preferences[keyWorkFinishTime] = finishTime
                withContext(Dispatchers.Main) {
                    logd("Initialized workFinishTime: $finishTime")
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

    open class Action {
        class DialogAction(val type: String): Action()
    }
}