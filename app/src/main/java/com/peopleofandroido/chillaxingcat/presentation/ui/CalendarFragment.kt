package com.peopleofandroido.chillaxingcat.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.FragmentCalendarBinding
import com.peopleofandroido.chillaxingcat.presentation.component.calendar.DayViewContainer
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment : BaseBindingFragment<FragmentCalendarBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarviewCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    // Show the month dates. Remember that views are recycled!
                    textView.visibility = View.VISIBLE
                    if (day.date == container.selectedDate) {
                        // If this is the selected date, show a round background and change the text color.
                        textView.setTextColor(Color.WHITE)
//                        textView.setBackgroundResource(R.drawable.selection_background)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        textView.setTextColor(Color.BLACK)
                        textView.setBackgroundColor(Color.GRAY)
//                        textView.background = null
                    }
                } else {
                    // Hide in and out dates
//                    textView.visibility = View.INVISIBLE
                }
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarviewCalendar.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarviewCalendar.scrollToMonth(currentMonth)

        binding.calendarviewCalendar.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }

        binding.calendarviewCalendar.setup(YearMonth.of(2021, 1), YearMonth.of(2021, 12), firstDayOfWeek)
    }

    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek is not DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
//            return = rhs + lhs //compile error
        }
        return daysOfWeek
    }

    class MonthViewContainer(view: View) : ViewContainer(view) {
        val textView = view.findViewById<TextView>(R.id.headerTextView)
    }
}