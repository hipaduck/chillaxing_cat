package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        viewModelScope.launch() {
            val result = useCases.isRequiredValuesEntered()
            result.data?.let { it ->
                if(it) {
                    moveToUserSetting()
                }
            }
            logd("getIsAppFirstLaunched()::data: ${result.data}, message: ${result.message}, status: ${result.status}")
        }
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}