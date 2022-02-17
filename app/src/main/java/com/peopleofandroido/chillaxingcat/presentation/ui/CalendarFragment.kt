package com.peopleofandroido.chillaxingcat.presentation.ui

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.common.daysOfWeekFromLocale
import com.peopleofandroido.chillaxingcat.common.getColorCompat
import com.peopleofandroido.chillaxingcat.common.setTextColorRes
import com.peopleofandroido.chillaxingcat.databinding.DialogDayRecordBinding
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
    var i = 0;

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
                setTextColorRes(R.color.accentNormal)
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

                val dialogBinding = DataBindingUtil.inflate<DialogDayRecordBinding>(
                    LayoutInflater.from(context), R.layout.dialog_day_record, null, false)
                dialogBinding.vm = binding.vm
                val dialog = DayDataDialog(requireContext(), dialogBinding)
                dialog.show()
            }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
//                    if (today == day.date) { // 오늘 날짜 체크 표시
//                        textView.setTextColorRes(R.color.backgroundLight)
//                        textView.setBackgroundResource(R.drawable.button_square_round_corner)
//                    } else { // 기본 날짜 색상
//                        textView.setTextColorRes(R.color.black)
////                        textView.setBackgroundResource(R.drawable.line_square_round_corner)
//                    }
                    when {
                        container.selectedDates.contains(day.date) -> { // 선택된 날짜 표시
                            textView.setTextColorRes(R.color.backgroundLight)
                            textView.setBackgroundResource(R.drawable.background_cat)
                            var color: Int = R.color.dayStatusRed
                            i++
                            when {
                                i % 5 == 1 ->
                                    color = R.color.dayStatusRed
                                i % 5 == 2 ->
                                    color = R.color.dayStatusOrange
                                i % 5 == 3 ->
                                    color = R.color.dayStatusYellow
                                i % 5 == 4 ->
                                    color = R.color.dayStatusGreen
                                i % 5 == 0 ->
                                    color = R.color.dayStatusBlue
                            }
                            context?.let {
                                textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it, color))
                            }
                        }
                        today == day.date -> { // 오늘 날짜 체크 표시
                            textView.setTextColorRes(R.color.black)
                            textView.setBackgroundResource(R.drawable.line_circle)
                        }
                        else -> { // 기본 날짜 색상
                            textView.setTextColorRes(R.color.black)
//                            textView.setBackgroundResource(R.drawable.line_square_round_corner)
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
        setStatusBarTheme()
    }

    override fun onStop() {
        super.onStop()
        setStatusBarTheme()
    }

    private fun setStatusBarTheme() {
        val window = requireActivity().window
        window.statusBarColor = requireContext().getColorCompat(R.color.backgroundLight)

        if (Build.VERSION.SDK_INT < 30) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            val controller: WindowInsetsController? =
                window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }
}