package com.hipaduck.base.common

import androidx.lifecycle.Observer
/**
 * Event를 observe 할 때 it.getContentIfNotHandled()?.let {} 의 코드를 줄이기 위함
 * */
class EventObserver<T>(private val onEventUnhandledContent : (T?) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event ?: return
        if (event.content == null && !event.hasBeenHandled) {
            //nullalbe 한 이벤트가 필요할 것을 대비하여 content가 null이더라도 처리할 수 있도록 함
            event.getContentIfNotHandled()
            onEventUnhandledContent(null)
            return
        }

        event.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}