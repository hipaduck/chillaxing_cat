package com.hipaduck.chillaxingcat.presentation.viewmodel

import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kizitonwose.calendarview.utils.yearMonth
import com.hipaduck.base.common.NavManager
import com.hipaduck.base.domain.Status
import com.hipaduck.base.util.NotNullMutableLiveData
import com.hipaduck.base.util.logd
import com.hipaduck.base.util.loge
import com.hipaduck.chillaxingcat.R
import com.hipaduck.chillaxingcat.domain.UseCases
import com.hipaduck.chillaxingcat.domain.model.RestTimeModel
import com.hipaduck.chillaxingcat.presentation.enums.TodayStatus
import com.hipaduck.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainMenuViewModel(
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _catStatus: NotNullMutableLiveData<TodayStatus> = NotNullMutableLiveData(TodayStatus.Normal)
    val catStatus: NotNullMutableLiveData<TodayStatus>
        get() = _catStatus

    private val _holidayDDayFromToday: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val holidayDDayFromToday: NotNullMutableLiveData<String>
        get() = _holidayDDayFromToday

    private var _criteriaDDayDate: LocalDate = LocalDate.now()


    init {
        checkFirstTime()
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
                        val addRestingTimeResult = useCases.addRestTime(RestTimeModel(date.toInt(), history, calculateTotalTime(history)))
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
                            val addRestingTimeResult = useCases.addRestTime(
                                RestTimeModel(
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
                    Toast.makeText(getApplication(), getApplication<Application>().getText(R.string.main_rest_finished_toast_message), Toast.LENGTH_SHORT).show()
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

    open class Action {
        class DialogAction(val type: String): Action()
    }
}