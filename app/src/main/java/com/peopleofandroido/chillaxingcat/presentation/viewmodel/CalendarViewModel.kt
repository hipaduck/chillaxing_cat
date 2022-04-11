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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
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

    val criteriaChillaxingLength: Long by lazy {
        loadCriteriaChillaxingLength()
    }

    val historicalDates: MutableList<LocalDate> = mutableListOf()
    val holidaysMap: MutableMap<LocalDate, String> = mutableMapOf()
    val chillaxingLengthInDay: MutableMap<LocalDate, Long> = mutableMapOf() // 하루의 쉼의 시간을 Long으로 반영

    init {
        val prevYearMonth = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"))
        val nextYearMonth = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"))
        val yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
        logd("current yyyyMM: $yyyyMM")
        loadComponentInCalendar(prevYearMonth, nextYearMonth, true)
        loadChillaxingLengths()
    }

    // todo 현재 usecase가 만들어지지 않아서 mock으로 진행
    private fun loadChillaxingLengths() {
        chillaxingLengthInDay[LocalDate.of(2022, 3, 14)] = 2 * 60 * 60 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 3, 20)] = 1 * 60 * 60 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 3, 18)] = 1 * 30 * 30 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 4, 1)] = 5 * 60 * 60 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 4, 7)] = 4 * 60 * 60 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 4, 9)] = 3 * 60 * 60 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 4, 3)] = 6 * 60 * 60 * 1_000L
        chillaxingLengthInDay[LocalDate.of(2022, 4, 11)] = 3 * 60 * 60 * 1_000L + 11 * 60 * 1_000L + 25 * 1_000L
    }

    // todo 현재 usecase가 만들어지지 않아서 mock으로 진행
    private fun loadCriteriaChillaxingLength(): Long = DEFAULT_CHILLAXING_LENGTH

    fun storeSpecifiedDayRecord(day: LocalDate, hours: Int, minutes: Int) {
        // 지정한 날의 시간과 분을 저장한다
    }

    /**
     * param format(example): yyyyMM(202204)
     */
    fun loadComponentInCalendar(startMonth: String, endMonth: String, init: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            loadRestingDaysInMonth(startMonth.toInt(), endMonth.toInt())
            loadHolidaysInMonth(startMonth, endMonth)
            if (init) {
                withContext(Dispatchers.Main) {
                    _actionEvent.value = Event(Action.CalendarAction("fill_days"))
                }
            }
        }
    }

    // new
    private suspend fun loadRestingDaysInMonth(prevMonth: Int, nextMonth: Int) {
        val result = useCases.findOutRestingDaysInMonth(prevMonth, nextMonth)
        when (result.status) {
            Status.SUCCESS -> {
                logd("list: ${result.data?.size}")
                result.data?.let {
                    for (dayInt in it) {
                        if (dayInt.toString().length >= 8) {
                            historicalDates.add(LocalDate.parse(dayInt.toString(), DateTimeFormatter.ofPattern("yyyyMMdd")))
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
                            holidaysMap[LocalDate.parse(dateModel.id.toString(), DateTimeFormatter.ofPattern("yyyyMMdd"))] = dateModel.name
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
        class CalendarAction(val type: String): Action()
    }
}