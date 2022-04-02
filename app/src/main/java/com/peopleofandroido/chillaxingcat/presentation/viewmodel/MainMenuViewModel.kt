package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.dataStore
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    private val keyFirstTime = booleanPreferencesKey("app_first_initialized")

    init {
        checkFirstTime()
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

    // notification enabled usecase 관련 테스트 코드 1
    fun startWork() {
        // 일 시작
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCases.getNotificationStatus()
            logd("getNotificationStatus()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    // notification enabled usecase 관련 테스트 코드 2
    fun finishWork() {
        // 일 마침
        viewModelScope.launch(Dispatchers.IO) {
            val currentNotiResult = useCases.getNotificationStatus()
            currentNotiResult.data?.let { notiResult ->
                val result = useCases.putNotificationStatus(!notiResult)
                result.data?.let {
                    if (it)
                        logd("putNotificationStatus(): success")
                    else
                        logd("putNotificationStatus(): fail")
                } ?: run {
                    logd("putNotificationStatus(): error")
                }
            }
        }
    }

    fun moveToUserSetting() {
        viewModelScope.launch {
            val navigationDirection = MainMenuFragmentDirections.actionMainMenuFragmentToUserSettingFragment(inputType = "initial")
            navManager.navigate(navigationDirection)
        }
    }

    private fun checkFirstTime() {
        viewModelScope.launch(Dispatchers.Main) {
            val data = getApplication<Application>().dataStore.data
                .catch {
                    logd("There is no Data")
                    _actionEvent.value = Event(Action.DialogAction("main_time_setting_dialog"))
                }.map { preferences ->
                    logd("preferences $preferences")
                    preferences[keyFirstTime] ?: true
                }

            val isFirstTime = data.first()
            if (isFirstTime) {
                getApplication<Application>().dataStore.edit { preferences ->
                    preferences[keyFirstTime] = false
                }
                moveToUserSetting()
            }
        }
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}