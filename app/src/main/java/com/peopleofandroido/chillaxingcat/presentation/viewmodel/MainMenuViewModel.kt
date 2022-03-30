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
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.Dispatchers
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

        dbTestGetDayOff()
        dbTestGetHoliday()
        dbTestGetRestingTime()
//        dbTestGetDayOffWithPeriod()
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

    fun test() {
        viewModelScope.launch (Dispatchers.IO) {
            val startPeriod = "202201"
            val endPeriod = "202203"
            val holidayResult = useCases.getHolidayWithPeriod(startPeriod, endPeriod)
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

    fun dbTestGetRestingTime() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCases.getRestingTime(20220207)

            withContext(Dispatchers.Main) {
                logd("Got RestingTime ${result.data}")
            }
        }
    }

    fun dbTestGetDayOff() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCases.getDayOff(20220207)
            withContext(Dispatchers.Main) {
                logd("Got DayOff ${result.data}")
            }
        }
    }

    fun dbTestGetHoliday() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCases.getHoliday(20220207)

            withContext(Dispatchers.Main) {
                logd("Got Holiday ${result.data}")
            }
        }
    }

//    fun dbTestGetDayOffWithPeriod() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val startingYearMonth = Period(2020, 9)
//            val endingYearMonth = Period(2023, 9)
//            val result = useCases.getDayOffWithPeriod(startingYearMonth, endingYearMonth)
//            withContext(Dispatchers.Main) {
//                logd("Got Holiday Period ${result.data}")
//            }
//        }
//    }
}