package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.domain.model.SettingMenuItemModel
import com.peopleofandroido.chillaxingcat.presentation.ui.SettingFragmentDirections
import kotlinx.coroutines.launch


class SettingViewModel(
    application: Application,
    private val navManager : NavManager
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent
    val settingMenuItemList: NotNullMutableLiveData<MutableList<SettingMenuItemModel>> = NotNullMutableLiveData(mutableListOf())

    init {
        refreshMenuItem()
    }

    private fun refreshMenuItem() {
        settingMenuItemList.value.clear()

        settingMenuItemList.value.add(SettingMenuItemModel(MENU_ID_USER_INFO_CHANGE, getApplication<Application>().getString(R.string.setting_modify_user_info), 10))
        settingMenuItemList.value.add(SettingMenuItemModel(MENU_ID_DEVELOPER_INTRODUCTION, getApplication<Application>().getString(R.string.setting_developer_introduce), 20))
        settingMenuItemList.value.add(SettingMenuItemModel(MENU_ID_SEND_EMAIL, getApplication<Application>().getString(R.string.setting_send_email), 30))
        settingMenuItemList.value.add(SettingMenuItemModel(MENU_ID_RATING_THIS_APP, getApplication<Application>().getString(R.string.setting_go_to_rate_the_app), 40))
        settingMenuItemList.value.add(SettingMenuItemModel(MENU_ID_OPENSOURCE, getApplication<Application>().getString(R.string.setting_opensource), 50))

        settingMenuItemList.value.sortBy { it.order }
    }

    private fun sendEmail() {
        val application = getApplication<Application>()
        val to = application.getString(R.string.email_receiver)
        val subject = application.getString(R.string.email_title)
        val body = application.getString(R.string.email_text)
        val mailTo = "mailto:" + to +
                "?&subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(body)
        val emailIntent = Intent(Intent.ACTION_VIEW)
        emailIntent.data = Uri.parse(mailTo)
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(emailIntent)
    }

    private fun moveToOpensource() { }

    private fun goToGooglePlay(){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=" + getApplication<Application>().applicationContext.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(intent)
    }

    fun processMenu(menuId : Long?) = when (menuId) {
        MENU_ID_USER_INFO_CHANGE -> moveToUserSetting()
        MENU_ID_RATING_THIS_APP -> goToGooglePlay()
        MENU_ID_OPENSOURCE -> moveToOpensource()
        MENU_ID_SEND_EMAIL -> sendEmail()
        MENU_ID_DEVELOPER_INTRODUCTION -> moveToDeveloperIntroduction()

        else -> actionEvent.value = Event(Action.ToastAction("메뉴 에러"))
    }

    private fun moveToUserSetting() {
        viewModelScope.launch {
            val navigationDirection = SettingFragmentDirections.actionSettingFragmentToUserSettingFragment(inputType = "setting")
            navManager.navigate(navigationDirection)
        }
    }

    private fun moveToDeveloperIntroduction() {
        viewModelScope.launch {
            val navigationDirection = SettingFragmentDirections.actionSettingFragmentToDeveloperFragment()
            navManager.navigate(navigationDirection)
        }
    }

    companion object {
        const val MENU_ID_USER_INFO_CHANGE = 1L
        const val MENU_ID_RATING_THIS_APP = 2L
        const val MENU_ID_SEND_EMAIL = 3L
        const val MENU_ID_OPENSOURCE = 4L
        const val MENU_ID_DEVELOPER_INTRODUCTION = 5L
    }

    open class Action {
        class DialogAction(val type: String): Action()
        class ToastAction(val msg: String): Action()
    }
}