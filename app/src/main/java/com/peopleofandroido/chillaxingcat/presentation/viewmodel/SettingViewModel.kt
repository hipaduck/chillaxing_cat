package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mikepenz.aboutlibraries.LibsBuilder
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.chillaxingcat.presentation.ui.SettingFragmentDirections
import kotlinx.coroutines.launch


class SettingViewModel(
    application: Application,
    private val navManager : NavManager
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    fun moveToUserSetting() {
        viewModelScope.launch {
            val navigationDirection = SettingFragmentDirections.actionSettingFragmentToUserSettingFragment(inputType = "setting")
            navManager.navigate(navigationDirection)
        }
    }

    fun showJobSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("setting_job_setting_dialog"))
    }

    fun showPushSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("setting_push_setting_dialog"))
    }

    fun moveToOpenSourceLicenseMenu() {
        val builder = LibsBuilder()
        builder.apply {
            activityTitle = getApplication<Application>().getString(getStringId("title_open_source_license"))
            aboutAppName = getApplication<Application>().getString(getStringId("app_name"))
            aboutShowIcon = true
            showLoadingProgress = true
            showLicense = true
        }.start(getApplication())
    }

    private fun getStringId(strName: String) : Int =
        getApplication<Application>().resources.getIdentifier(strName, "string", getApplication<Application>().packageName)

    open class Action {
        class DialogAction(val type: String): Action()
    }
}