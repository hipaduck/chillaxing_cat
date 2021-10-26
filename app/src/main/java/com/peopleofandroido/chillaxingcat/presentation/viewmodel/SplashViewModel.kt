package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.BaseViewModel
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.chillaxingcat.presentation.ui.SplashFragmentDirections
import kotlinx.coroutines.launch

class SplashViewModel(
    private val navManager : NavManager
) : BaseViewModel() {

    fun moveToMain() {
        viewModelScope.launch {
            val navigationDirection = SplashFragmentDirections.actionSplashFragmentToMainMenuFragment()
            navManager.navigate(navigationDirection)
        }
    }
}