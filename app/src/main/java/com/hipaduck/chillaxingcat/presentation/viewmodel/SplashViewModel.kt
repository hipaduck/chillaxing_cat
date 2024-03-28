package com.hipaduck.chillaxingcat.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.hipaduck.base.common.BaseViewModel
import com.hipaduck.base.common.NavManager
import com.hipaduck.chillaxingcat.presentation.ui.SplashFragmentDirections
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