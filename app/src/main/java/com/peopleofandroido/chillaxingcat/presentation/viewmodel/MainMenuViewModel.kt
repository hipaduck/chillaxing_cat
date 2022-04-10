package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainMenuViewModel(
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    private val _isStartButtonVisible: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val isStartButtonVisible: NotNullMutableLiveData<Boolean>
        get() = _isStartButtonVisible
    private val _isPauseButtonVisible: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isPauseButtonVisible: NotNullMutableLiveData<Boolean>
        get() = _isPauseButtonVisible
    private val _isResumeButtonVisible: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isResumeButtonVisible: NotNullMutableLiveData<Boolean>
        get() = _isResumeButtonVisible
    private val _isStopButtonVisible: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val isStopButtonVisible: NotNullMutableLiveData<Boolean>
        get() = _isStopButtonVisible


    init {
        checkFirstTime()
        checkTodayState()
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

    private fun storeTodayDate(time: String) {
        viewModelScope.launch {
            val result = useCases.putTodayDate(time)
            result.data?.let {
                if (it) {
                    logd("putTodayDate(): success : $time")
                } else
                    logd("putTodayDate(): fail")
            } ?: run {
                logd("putTodayDate(): error")
            }
        }
    }

    private fun storeTodayHistory(history: String) {
        viewModelScope.launch {
            val result = useCases.putTodayHistory(history)
            result.data?.let {
                if (it) {
                    logd("putTodayHistory(): success : $history")
                } else
                    logd("putTodayHistory(): fail")
            } ?: run {
                logd("putTodayHistory(): error")
            }
        }
    }

    private fun storeTodayStatus(status: String) {
        viewModelScope.launch {
            val result = useCases.putTodayStatus(status)
            result.data?.let {
                if (it) {
                    logd("putTodayStatus(): success : $status")
                } else
                    logd("putTodayStatus(): fail")
            } ?: run {
                logd("putTodayStatus(): error")
            }
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

    private fun checkTodayState() {
        viewModelScope.launch() {
            val todayDateResult = useCases.getTodayDate()
            todayDateResult.data?.let { date ->
                val todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                if (date == todayDate) {
                    val todayStatusResult = useCases.getTodayStatus()
                    todayStatusResult.data?.let { todayStatus ->
                        logd("getTodayStatus(): $todayStatus")
                        changeButtonUiForTodayState(todayStatus)
                    }
                } else {
                    storeTodayDate(todayDate)
                    storeTodayHistory("")
                    storeTodayStatus("")
                    //이전 기록 db 저장
                }
            }
            logd("getTodayDate()::data: ${todayDateResult.data}, message: ${todayDateResult.message}, status: ${todayDateResult.status}")
        }
    }

    fun startResting() {
        val status = "rest"
        storeTodayStatus(status)
        changeButtonUiForTodayState(status)
        //TimeStamp 저장
        viewModelScope.launch() {
            storeTodayHistory(System.currentTimeMillis().toString())
        }
    }

    fun pauseResting() {
        val status = "pause"
        storeTodayStatus("pause")
        changeButtonUiForTodayState(status)
        //TimeStamp 저장
        viewModelScope.launch() {
            val getTodayHistoryResult = useCases.getTodayHistory()
            getTodayHistoryResult.data?.let { todayHistory ->
                logd("getTodayHistory(): $todayHistory")
                storeTodayHistory(todayHistory + "-" + System.currentTimeMillis().toString() + "|")
            } ?: run {
                logd("getTodayHistory(): error")
            }
        }
    }

    fun resumeResting() {
        val status = "rest"
        storeTodayStatus("rest")
        changeButtonUiForTodayState(status)
        //TimeStamp 저장
        viewModelScope.launch() {
            val getTodayHistoryResult = useCases.getTodayHistory()
            getTodayHistoryResult.data?.let { todayHistory ->
                logd("getTodayHistory(): $todayHistory")
                storeTodayHistory(todayHistory +  System.currentTimeMillis().toString())
            } ?: run {
                logd("getTodayHistory(): error")
            }
        }
    }

    fun stopResting() {
        //TimeStamp 저장 + db로 저장
        viewModelScope.launch() {
            val getTodayStatus = useCases.getTodayStatus()
            getTodayStatus.data?.let { status ->
                logd("getTodayStatus(): $status")
                if (status != "finish") {
                    storeTodayStatus("finish")
                    changeButtonUiForTodayState("finish")

                    val getTodayHistoryResult = useCases.getTodayHistory()
                    getTodayHistoryResult.data?.let { todayHistory ->
                        var saveHistory = todayHistory
                        if (!todayHistory.endsWith("|")) {
                            saveHistory = todayHistory + "-" +  System.currentTimeMillis().toString() + "|"
                        }
                        logd("getTodayHistory(): $todayHistory")
                        storeTodayHistory(saveHistory)
                    } ?: run {
                        logd("getTodayHistory(): error")
                    }
                } else {
                    Toast.makeText(getApplication(), getApplication<Application>().getText(R.string.main_resting_finished_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changeButtonUiForTodayState (state: String) {
        when(state) {
            "" -> {
                isStartButtonVisible.value = true
                isPauseButtonVisible.value = false
                isResumeButtonVisible.value = false
                isStopButtonVisible.value = false
            }
            "rest" -> {
                isStartButtonVisible.value = false
                isPauseButtonVisible.value = true
                isResumeButtonVisible.value = false
                isStopButtonVisible.value = true
            }
            "pause" -> {
                isStartButtonVisible.value = false
                isPauseButtonVisible.value = false
                isResumeButtonVisible.value = true
                isStopButtonVisible.value = true
            }
            "finish" -> {
                isStartButtonVisible.value = false
                isPauseButtonVisible.value = false
                isResumeButtonVisible.value = false
                isStopButtonVisible.value = true
            }
        }
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}