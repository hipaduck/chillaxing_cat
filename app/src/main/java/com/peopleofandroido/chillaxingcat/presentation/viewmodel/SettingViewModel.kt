package com.peopleofandroido.chillaxingcat.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peopleofandroido.base.common.Event
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.base.util.NotNullMutableLiveData
import com.peopleofandroido.chillaxingcat.domain.UseCases
import com.peopleofandroido.chillaxingcat.domain.model.UserInfoModel
import kotlinx.coroutines.launch

class SettingViewModel(
    application: Application,
    private val navManager : NavManager,
    private val useCases: UseCases
) : AndroidViewModel(application) {
    private val _actionEvent: NotNullMutableLiveData<Event<Action>> = NotNullMutableLiveData(Event(Action()))
    val actionEvent: NotNullMutableLiveData<Event<Action>>
        get() = _actionEvent

    fun showTimeSettingDialog() {
        _actionEvent.value = Event(Action.DialogAction("time_setting_dialog"))
    }

//    fun getUserInfo() : UserInfoModel {
//        viewModelScope.launch {
//            useCases.getUserInfo()
//        } //???????? 이건 Main에서도 쓸텐데 아예 시간 관리하는 애를 하나 따로 뺄까 (db 구성부터도 고민이 필요할 것 같음)
//    }

    open class Action {
        class DialogAction(val type: String): Action()
    }
}