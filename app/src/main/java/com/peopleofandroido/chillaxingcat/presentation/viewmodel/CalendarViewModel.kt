package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import com.peopleofandroido.base.common.BaseViewModel
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.chillaxingcat.domain.UseCases

class CalendarViewModel(
    private val navManager : NavManager,
    private val useCases: UseCases
) : BaseViewModel() {
}