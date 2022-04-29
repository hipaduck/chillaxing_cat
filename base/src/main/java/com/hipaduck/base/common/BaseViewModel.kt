package com.hipaduck.base.common

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() { //상속이 가능하도록 open

    override fun onCleared() {
        super.onCleared()
    }
}