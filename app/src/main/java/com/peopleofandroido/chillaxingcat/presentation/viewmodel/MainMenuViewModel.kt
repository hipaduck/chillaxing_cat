package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendarview.utils.yearMonth
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.domain.Status
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.base.util.loge
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

    private val _holidayDDayFromToday: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val holidayDDayFromToday: NotNullMutableLiveData<String>
        get() = _holidayDDayFromToday

    private var _criteriaDDayDate: LocalDate = LocalDate.now()


    init {
        checkFirstTime()
        checkTodayState()
        updateDecimalDayFromClosestHoliday()
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

    private fun updateDecimalDayFromClosestHoliday() {
        // 0. 갱신해야 하는지 여부를 체크(D-Day값이 비어있지 않고, 기준날짜가 오늘과 같을 경우는 갱신 안함)
        if (_holidayDDayFromToday.value.isNotEmpty() && _criteriaDDayDate == LocalDate.now()) return

        // 1. 오늘 날짜를 처음 기준으로 설정
        var currentDay = LocalDate.now()
        val holidayList = mutableListOf<LocalDate>()
        viewModelScope.launch(Dispatchers.IO) {
            // 2. 공휴일 List 안에 1개 미만인 경우, 계속 얻어오기 시도
            while (holidayList.size < 1) {
                // 3. 해당월의 공휴일 얻어오기
                val yyyyMM = currentDay.format(DateTimeFormatter.ofPattern("yyyyMM"))
                val result = useCases.getHolidayWithPeriod("${yyyyMM.substring(0,4)}${yyyyMM.substring(4,6)}",
                    "${yyyyMM.substring(0,4)}${yyyyMM.substring(4,6)}")

                when (result.status) {
                    Status.SUCCESS -> {
                        logd("list: ${result.data?.size}")
                        result.data?.let {
                            for (dateModel in it) {
                                if (dateModel.id.toString().length >= 8) {
                                    val localDate = LocalDate.parse(dateModel.id.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))
                                    logd("localDate: $localDate")
                                    holidayList.add(localDate)
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        loge("error: ${result.message}")
                    }
                }

                // 4. 만약 currentMonth가 현재 달과 같다면, 오늘날짜를 포함하여 이후날짜만 포함시킴
                if (currentDay.yearMonth == LocalDate.now().yearMonth) {
                    holidayList.filter { it >= LocalDate.now() }
                }

                // 5. 공휴일이 1개 이상일 경우 오른차순 정렬, 아니라면 currentDay를 한 달 증가시킴
                if (holidayList.size > 1) {
                    holidayList.sort()
                } else {
                    currentDay = currentDay.plusMonths(1)
                }

                // 6. 1년 이상의 탐색은 의미가 없으므로, 1년이 넘도록 값을 조회하지 못한다면, 중단함
                if (ChronoUnit.MONTHS.between(LocalDate.now(), currentDay) > 12) break
            }

            // 7. 가장 가까운 공휴일과 오늘의 차이를 구해서 D-Day에 반영 및 기준일자도 갱신해주면 마무리
            withContext(Dispatchers.Main) {
                _holidayDDayFromToday.value = ChronoUnit.DAYS.between(LocalDate.now(), holidayList[0]).toString()
                _criteriaDDayDate = LocalDate.now()
            }
        }
    }

    private fun changeButtonUiForTodayState (state: String) {
        when(state) {
            "" -> {
                _isStartButtonVisible.value = true
                _isPauseButtonVisible.value = false
                _isResumeButtonVisible.value = false
                _isStopButtonVisible.value = false
            }
            "rest" -> {
                _isStartButtonVisible.value = false
                _isPauseButtonVisible.value = true
                _isResumeButtonVisible.value = false
                _isStopButtonVisible.value = true
            }
            "pause" -> {
                _isStartButtonVisible.value = false
                _isPauseButtonVisible.value = false
                _isResumeButtonVisible.value = true
                _isStopButtonVisible.value = true
            }
            "finish" -> {
                _isStartButtonVisible.value = false
                _isPauseButtonVisible.value = false
                _isResumeButtonVisible.value = false
                _isStopButtonVisible.value = true
            }
        }
    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}