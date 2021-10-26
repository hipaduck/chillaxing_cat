package com.peopleofandroido.base.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel

open class BaseApplicationViewModel(application: Application) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
    }
}