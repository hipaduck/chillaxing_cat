package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.BaseViewModel
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.domain.Status
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.base.util.loge
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.model.YearMonth
import com.peopleofandroido.chillaxingcat.presentation.ui.MainMenuFragmentDirections
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val navManager : NavManager,
    private val useCases: UseCases
) : BaseViewModel() {
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
            val yearMonth = YearMonth(2021, 9)
            val holidayResult = useCases.getHoliday(yearMonth)
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
}