package com.peopleofandroido.chillaxingcat.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.children
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.yearMonth
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.common.daysOfWeekFromLocale
import com.peopleofandroido.chillaxingcat.common.getColorCompat
import com.peopleofandroido.chillaxingcat.common.setTextColorRes
import com.peopleofandroido.chillaxingcat.databinding.FragmentCalendarBinding
import com.peopleofandroido.chillaxingcat.presentation.component.calendar.DayViewContainer
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CalendarFragment : BaseBindingFragment<FragmentCalendarBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_calendar

    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

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

        val daysOfWeek = daysOfWeekFromLocale()
        (binding.layoutLegend.root as ViewGroup).children.forEachIndexed { index, v ->
            (v as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .uppercase(Locale.getDefault())
                setTextColorRes(R.color.design_default_color_secondary_variant)
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        binding.calendarviewCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarviewCalendar.scrollToMonth(currentMonth)

        binding.calendarviewCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view) {day ->
                binding.calendarviewCalendar.notifyDayChanged(day)
            }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        container.selectedDates.contains(day.date) -> { // 선택된 날짜 표시
                            textView.setTextColorRes(R.color.design_default_color_primary)
                            textView.setBackgroundResource(R.drawable.background_square_round_corner)
                        }
                        today == day.date -> { // 오늘 날짜 체크 표시
                            textView.setTextColorRes(R.color.design_default_color_primary_dark)
                            textView.setBackgroundResource(R.drawable.button_square_round_corner)
                        }
                        else -> { // 기본 날짜 색상
                            textView.setTextColorRes(R.color.design_default_color_on_primary)
                            textView.background = null
                        }
                    }
                } else { // 선택한 달에 해당하지 않는 날짜들의 색상
                    textView.setTextColorRes(R.color.material_on_primary_disabled)
                    textView.background = null
                }
            }
        }

        binding.calendarviewCalendar.monthScrollListener = {
            if (binding.calendarviewCalendar.maxRowCount == 6) {
                binding.tvCalendarCurrentYearText.text = it.yearMonth.year.toString()
                binding.tvCalendarCurrentMonthText.text = monthTitleFormatter.format(it.yearMonth)
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.tvCalendarCurrentYearText.text = firstDate.yearMonth.year.toString()
                    binding.tvCalendarCurrentMonthText.text = monthTitleFormatter.format(firstDate)
                } else {
                    binding.tvCalendarCurrentMonthText.text = "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(lastDate)}"
                    if (firstDate.year == lastDate.year) {
                        binding.tvCalendarCurrentYearText.text = firstDate.yearMonth.year.toString()
                    } else {
                        binding.tvCalendarCurrentYearText.text = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.design_default_color_primary_variant)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.design_default_color_primary_dark)
    }
}