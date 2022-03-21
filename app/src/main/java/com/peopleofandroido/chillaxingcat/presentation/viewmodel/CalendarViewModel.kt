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
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    val historicalDates: MutableList<LocalDate> = mutableListOf()
    val holidaysMap: MutableMap<LocalDate, String> = mutableMapOf()

    init {
        val yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
        logd("current yyyyMM: $yyyyMM")
        loadComponentInCalendar(yyyyMM)
    }

    private fun loadComponentInCalendar(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadRestingDaysInMonth(month.toInt())
            loadHolidaysInMonth(month)
            withContext(Dispatchers.Main) {
                _actionEvent.value = Event(Action.CalendarAction("fill_days"))
            }
        }
    }

    private suspend fun loadRestingDaysInMonth(month: Int) {
        val result = useCases.findOutRestingDaysInMonth(month)
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

    private suspend fun loadHolidaysInMonth(month: String) {
        val result = useCases.getHolidayWithPeriod(month, month)
        when (result.status) {
            Status.SUCCESS -> {
                logd("list: ${result.data?.size}")
                result.data?.let {
                    for (dateModel in it) {
                        if (dateModel.id.toString().length >= 8) {
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