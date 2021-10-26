package com.peopleofandroido.base.common

/**
 * ViewModel에서 View로 이벤트를 발생 시킬 때 사용하는 방법으로 LiveData 객체를 Event 로 감싸서 사용
 * */
class Event<out T>(val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}