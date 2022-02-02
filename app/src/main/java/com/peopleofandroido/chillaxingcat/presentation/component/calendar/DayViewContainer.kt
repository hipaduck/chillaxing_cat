package com.peopleofandroido.chillaxingcat.presentation.component.calendar

import android.util.Log
import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.peopleofandroido.chillaxingcat.R
import java.time.LocalDate

class DayViewContainer(view: View, val listener: (day: CalendarDay) -> Unit) : ViewContainer(view) {

    val textView = view.findViewById<TextView>(R.id.tv_calendar_day_text)!!
    // Will be set when this container is bound
    lateinit var day: CalendarDay

    val selectedDates = mutableSetOf<LocalDate>()

    init {
        view.setOnClickListener {
            // Check the day owner as we do not want to select in or out dates.
            Log.d("TEST", "day.date: ${day.date}")
            Log.d("TEST", "  selectedDates: ${selectedDates.size}}")
            if (day.owner == DayOwner.THIS_MONTH) {
                if (selectedDates.contains(day.date)) {
                    selectedDates.remove(day.date)
                } else {
                    selectedDates.add(day.date)
                }

                listener.invoke(day)
            }
        }
    }
}