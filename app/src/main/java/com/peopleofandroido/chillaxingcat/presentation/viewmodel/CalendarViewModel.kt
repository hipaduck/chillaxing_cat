package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.BaseViewModel
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.domain.Status
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.base.util.loge
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarViewModel(
    private val navManager : NavManager,
    private val useCases: UseCases
) : BaseViewModel() {
    companion object {
        const val DEFAULT_CHILLAXING_LENGTH = 5 * 60 * 60 * 1_000L // 5시간
    }
    // 쉬는시간 >= 목표시간 * 1: 파랑, 쉬는시간 >= 목표시간 * 0.8: 녹색, 쉬는시간 >= 목표시간 * 0.5: 노랑
    // 쉬는시간 >= 목표시간 * 0.3: 오렌지, else(쉬는시간 < 목표시간 * 0.3: 빨강)
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    var criteriaChillaxingLength: Long = DEFAULT_CHILLAXING_LENGTH

//    val historicalDates: MutableList<LocalDate> = mutableListOf()
    val holidaysMap: MutableMap<LocalDate, String> = mutableMapOf()
    val chillaxingLengthInDayMap: MutableMap<LocalDate, Long> = mutableMapOf() // 하루의 쉼의 시간을 Long으로 반영
    val chillaxingRecordInDayMap: MutableMap<LocalDate, String> = mutableMapOf() // 하루의 데이터를 저장(위 데이터와 통합 필요)

    init {
        val prevYearMonth = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"))
        val nextYearMonth = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"))
        val yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
        logd("current yyyyMM: $yyyyMM")
        loadCriteriaChillaxingLength()
        loadComponentInCalendar(prevYearMonth, nextYearMonth, true)
    }

    // todo 현재 usecase가 만들어지지 않아서 mock으로 진행
    private fun loadCriteriaChillaxingLength() {
        viewModelScope.launch(Dispatchers.IO) {
            val goalResult = useCases.getGoalRestingTime()
            when (goalResult.status) {
                Status.SUCCESS -> {
                    logd("getGoalRestingTime succeed: ${goalResult.data}")
                    val originalStr = goalResult.data!!
//                    val localDateTime = LocalDateTime.parse(originalStr, DateTimeFormatter.ofPattern("HH:mm"))
                    val hour = originalStr.substring(0, 2).toInt()
                    val minute = originalStr.substring(3, 4).toInt()
                    criteriaChillaxingLength = hour * 60 * 60 * 1_000L + minute * 60 * 1_000L
                    logd("getGoalRestingTime(criteriaChillaxingLength) succeed: $criteriaChillaxingLength")
                }
                Status.ERROR -> {
                    loge("getGoalRestingTime failed: ${goalResult.message}")
                }
            }
        }
    }

    fun storeSpecifiedDayRecord(day: LocalDate, hours: Int, minutes: Int) {
        val yyyyMMdd = day.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val timestamp: Long = hours * 60 * 60 * 1_000L + minutes * 60 * 1_000L
        // 지정한 날의 시간과 분을 저장한다
        viewModelScope.launch(Dispatchers.IO) {
            val editResult = useCases.writeChillaxingTotalTime(yyyyMMdd.toInt(), timestamp)
            when (editResult.status) {
                Status.SUCCESS -> {
//                    historicalDates.add(day)
                    chillaxingLengthInDayMap[day] = timestamp // 해당 날짜 ViewModel 데이터 업데이트
                    logd("write succeed: ${editResult.data}")
                }
                Status.ERROR -> {
                    loge("write failed: ${editResult.message}")
                }
            }
        }
    }

    /**
     * param format(example): yyyyMM(202204)
     */
    fun loadComponentInCalendar(startMonth: String, endMonth: String, init: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            loadChillaxingDaysInMonth(startMonth.toInt(), endMonth.toInt())
            loadHolidaysInMonth(startMonth, endMonth)
            if (init) {
                withContext(Dispatchers.Main) {
                    _actionEvent.value = Event(Action.CalendarAction("fill_days"))
                }
            } else {
                withContext(Dispatchers.Main) {
                    val startDate = LocalDate.parse("${startMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
                    val endDate = LocalDate.parse("${endMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
                    var loopDate = startDate
                    while (loopDate <= endDate) {
                        val date = loopDate.format(DateTimeFormatter.ofPattern("yyyyMM"))

                        val yearMonth = YearMonth.parse(date.toString(), DateTimeFormatter.ofPattern("yyyyMM"))
                        _actionEvent.value = Event(Action.CalendarAction("notify_month", yearMonth))
                        logd("yearMonth notified: $yearMonth")

                        loopDate = loopDate.plusMonths(1)
                    }
                }
            }
        }
    }

    // new
    private suspend fun loadChillaxingDaysInMonth(prevMonth: Int, nextMonth: Int) {
        val result = useCases.findOutRestingDaysInMonth(prevMonth, nextMonth)
        when (result.status) {
            Status.SUCCESS -> {
                // 비우고 시작
//                historicalDates.clear()
//                chillaxingLengthInDayMap.clear()
                logd("list: ${result.data?.size}")
                result.data?.let {
                    for (model in it) {
                        if (model.id.toString().length >= 8) {
                            val localDate = LocalDate.parse(model.id.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))
//                            historicalDates.add(localDate)
                            chillaxingLengthInDayMap[localDate] = model.totalTime
                            chillaxingRecordInDayMap[localDate] = model.history

                            // 테스트를 위해 임시로 아래 코드 호출
//                            loadMockChillaxingLengths()
                        }
                    }
                }
            }
            Status.ERROR -> {
                loge("error: ${result.message}")
            }
        }
    }

    private suspend fun loadHolidaysInMonth(startMonth: String, endMonth: String) {
        logd("loadHoliday: $startMonth, $endMonth")
        val result = useCases.getHolidayWithPeriod(startMonth, endMonth)
        when (result.status) {
            Status.SUCCESS -> {
                logd("list: ${result.data?.size}")
                result.data?.let {
                    for (dateModel in it) {
                        if (dateModel.id.toString().length >= 8) {
                            logd("received: ${LocalDate.parse(dateModel.id.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))}")
                            val localDate = LocalDate.parse(dateModel.id.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))
                            holidaysMap[localDate] = dateModel.name
                        }
                    }
                }
            }
            Status.ERROR -> {
                loge("error: ${result.message}")
            }
        }
    }

    open class Action {
        class EventAction(val type: String): Action()
        class CalendarAction(val type: String, val yearMonth: YearMonth? = null): Action()
    }
}