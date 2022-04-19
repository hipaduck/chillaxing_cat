package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.presentation.enums.TodayStatus
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainMenuViewModel(
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _catStatus: NotNullMutableLiveData<TodayStatus> = NotNullMutableLiveData(TodayStatus.Normal)
    val catStatus: NotNullMutableLiveData<TodayStatus>
        get() = _catStatus

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
                } else {
                    checkTodayState()
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

                if (TextUtils.isEmpty(date)) {
                    logd("getTodayDate() is empty: $date")
                } else if (date == todayDate) {
                    val todayStatusResult = useCases.getTodayStatus()
                    todayStatusResult.data?.let { todayStatus ->
                        logd("getTodayStatus(): $todayStatus")
                        _catStatus.value = TodayStatus.typeOf(todayStatus)
                    }
                } else {
                    //이전 기록 db 저장
                    val todayHistory = useCases.getTodayHistory()
                    todayHistory.data?.let { history ->
                        val addRestingTimeResult = useCases.addRestingTime(RestingTimeModel(date.toInt(), history, calculateTotalTime(history)))
                        addRestingTimeResult.data?.let {
                            logd("addRestingTimeResult(): $it")
                        }
                    }

                    storeTodayDate(todayDate)
                    storeTodayHistory("")
                    storeTodayStatus("normal")
                }
            }
            logd("getTodayDate()::data: ${todayDateResult.data}, message: ${todayDateResult.message}, status: ${todayDateResult.status}")
        }
    }

    fun calculateTotalTime(history: String): Long {
        var totalTime: Long = 0L
        val timeList = history.split("|")

        for(time in timeList) {
            val timeStampList = time.split("-")
            if (timeStampList.size > 1) {
                if (!TextUtils.isEmpty(timeStampList[0]) && !TextUtils.isEmpty(timeStampList[1])) {
                    val timeStamp1 = Timestamp(timeStampList[0].toLong())
                    val timeStamp2 = Timestamp(timeStampList[1].toLong())
                    totalTime += (timeStamp2.time - timeStamp1.time)
                }
            }
        }
        logd("calculateTotalTime: totalTime : $totalTime")
        return totalTime
    }

    fun startResting() {
        _catStatus.value = TodayStatus.Rest
        storeTodayStatus(_catStatus.value.type)
        storeTodayDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        //TimeStamp 저장
        viewModelScope.launch() {
            storeTodayHistory(System.currentTimeMillis().toString())
        }
    }

    fun pauseResting() {
        _catStatus.value = TodayStatus.Pause
        storeTodayStatus(_catStatus.value.type)
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
        _catStatus.value = TodayStatus.Rest
        storeTodayStatus(_catStatus.value.type)
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
                    _catStatus.value = TodayStatus.Finish
                    storeTodayStatus(_catStatus.value.type)

                    val getTodayHistoryResult = useCases.getTodayHistory()
                    getTodayHistoryResult.data?.let { todayHistory ->
                        var saveHistory = todayHistory
                        if (!todayHistory.endsWith("|")) {
                            saveHistory = todayHistory + "-" +  System.currentTimeMillis().toString() + "|"
                        }
                        logd("getTodayHistory(): $todayHistory")
                        CoroutineScope(Dispatchers.IO).launch {
                            storeTodayHistory(saveHistory)
                            val todayDate =
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            val addRestingTimeResult = useCases.addRestingTime(
                                RestingTimeModel(
                                    todayDate.toInt(),
                                    saveHistory,
                                    calculateTotalTime(saveHistory)
                                )
                            )
                            addRestingTimeResult.message?.let {
                                logd("addRestingTimeResult(): error : $it")
                            }
                        }
                    } ?: run {
                        logd("getTodayHistory(): error")
                    }
                } else {
                    Toast.makeText(getApplication(), getApplication<Application>().getText(R.string.main_resting_finished_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}