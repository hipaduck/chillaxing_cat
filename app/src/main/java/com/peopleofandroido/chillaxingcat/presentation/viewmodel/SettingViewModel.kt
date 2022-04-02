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
import com.peopleofandroido.chillaxingcat.presentation.ui.SettingFragmentDirections
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
    private val keyPushAvailable = stringPreferencesKey("push_available")
    private val _pushAvailable: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val pushAvailable: NotNullMutableLiveData<Boolean>
        get() = _pushAvailable

    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent
    private var mCalender: GregorianCalendar? = null

    fun moveToUserSetting() {
        viewModelScope.launch {
            val navigationDirection = SettingFragmentDirections.actionSettingFragmentToUserSettingFragment(inputType = "setting")
            navManager.navigate(navigationDirection)
        }
    }

    fun showJobSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("setting_job_setting_dialog"))
    }

    fun showPushSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("setting_push_setting_dialog"))
    }


    open class Action {
        class DialogAction(val type: String): Action()
    }
}