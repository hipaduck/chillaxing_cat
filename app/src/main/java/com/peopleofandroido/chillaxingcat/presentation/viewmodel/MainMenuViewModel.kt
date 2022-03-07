package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.BaseViewModel
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.domain.Status
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.base.util.loge
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.model.DateModel
import com.peopleofandroido.chillaxingcat.domain.model.RestingTimeModel
import com.peopleofandroido.chillaxingcat.domain.model.YearMonth
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenuViewModel(
    private val navManager : NavManager,
    private val useCases: UseCases
) : BaseViewModel() {

    init {
        test()
        dbTestAddDayOff()
        dbTestAddHoliday()
        dbTestAddRestingTime()

        viewModelScope.launch {
            dbTestGetDayOff()
            dbTestGetHoliday()
            dbTestGetRestingTime()
        }
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

    fun test() {
        viewModelScope.launch {
            val yearMonth = YearMonth(2022, 9)
            val holidayResult = useCases.requestHoliday(yearMonth)
            when (holidayResult.status) {
                Status.SUCCESS -> {
                    logd("holiday data: ${holidayResult.data}")
                    holidayResult.data?.let {
                        logd("holiday string data: $it")
                    }
                }
                Status.ERROR -> {
                    loge("holiday error: ${holidayResult.message}")
                }
            }
        }
    }

    fun dbTestAddRestingTime() {
        val restingTime = RestingTimeModel(20220207, "history", "01:30")
        viewModelScope.launch (Dispatchers.IO) {
            useCases.addRestingTime(restingTime)
            logd("Resting Time added")
        }
    }

    fun dbTestAddHoliday() {
        val holiday = DateModel(20220207, "holiday")
        viewModelScope.launch (Dispatchers.IO) {
            useCases.addHoliday(holiday)
            logd("Holiday added")
        }
    }

    fun dbTestAddDayOff() {
        val dayOff = DateModel(20220207, "dayoff")
        viewModelScope.launch (Dispatchers.IO) {
            useCases.addDayOff(dayOff)
            logd("DayOff added")
        }
    }

    suspend fun dbTestGetRestingTime() {
        return withContext (Dispatchers.IO) {
            val runHoliday = async { useCases.getRestingTime(20220207) }
            val holidayList = runHoliday.await()

            logd("Got RestingTime $holidayList")
        }
    }

    suspend fun dbTestGetDayOff() {
        return withContext (Dispatchers.IO) {
            val runDayOffList = async { useCases.getDayOff(20220207) }
            val dayOffList = runDayOffList.await()

            logd("Got DayOff $dayOffList")
        }
    }

    suspend fun dbTestGetHoliday() {
        return withContext (Dispatchers.IO) {
            val runHoliday = async { useCases.getHoliday(20220207) }
            val holidayList = runHoliday.await()

            logd("Got Holiday $holidayList")
        }
    }
}