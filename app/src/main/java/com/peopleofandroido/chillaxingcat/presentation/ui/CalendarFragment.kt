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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.utils.yearMonth
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.base.util.logd
import com.peopleofandroido.base.util.px
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.common.daysOfWeekFromLocale
import com.peopleofandroido.chillaxingcat.common.getColorCompat
import com.peopleofandroido.chillaxingcat.common.setTextColorRes
import com.peopleofandroido.chillaxingcat.databinding.DialogDayRecordBinding
import com.peopleofandroido.chillaxingcat.databinding.FragmentCalendarBinding
import com.peopleofandroido.chillaxingcat.presentation.component.calendar.DayViewContainer
import com.peopleofandroido.chillaxingcat.presentation.viewmodel.CalendarViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.time.DayOfWeek
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
    var i = 0
//    var dayViewContainer: DayViewContainer? = null

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

        binding.vm?.actionEvent?.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { action ->
                when (action) {
                    is CalendarViewModel.Action.CalendarAction -> {
                        when (action.type) {
                            "fill_days" -> {
                                logd("try to initial the calendar view...")
                                initializeCalendarView()
                            }
                        }
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

    private fun initializeCalendarView() {
        val daysOfWeek = daysOfWeekFromLocale()
        (binding.layoutLegend.root as ViewGroup).children.forEachIndexed { index, v ->
            (v as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .uppercase(Locale.getDefault())
                if (daysOfWeek[index] == DayOfWeek.SATURDAY) {
                    setTextColorRes(R.color.calendarSaturday)
                } else if(daysOfWeek[index] == DayOfWeek.SUNDAY) {
                    setTextColorRes(R.color.calendarSundayHoliday)
                } else {
                    setTextColorRes(R.color.accentNormal)
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)

        // 아래의 코드를 실행 전에, 체크해야 하는 날짜의 데이터는 미리 가져와서 변수에 넣어 놓는다.
        // 그리고 calendar에 bind 되는 시점에 각 date를 loop 도는 동안, 해당 날짜를 필터링하여 원하는 색으로 변경해 놓는다.
        binding.calendarviewCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarviewCalendar.scrollToMonth(currentMonth)

        binding.calendarviewCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view) { day ->
                    logd("1 ${day.date}")
                    binding.calendarviewCalendar.notifyDayChanged(day)

                    if (today > day.date || binding.vm?.holidaysMap?.contains(day.date) == true) {
                        val dialogBinding = DataBindingUtil.inflate<DialogDayRecordBinding>(
                            LayoutInflater.from(context), R.layout.dialog_day_record, null, false
                        )
                        dialogBinding.vm = binding.vm
                        val dialog = DayDataDialog(requireContext(), dialogBinding)
                        dialog.show()
                    }
                }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    logd("holidaysMap: ${binding.vm?.holidaysMap?.size}")
                    logd("day.date: ${day.date}")
                    binding.vm?.let { vm ->
                        when {
                            !vm.chillaxingLengthInDay.containsKey(day.date) -> { // 키 자체가 없다면 기록이 없는 것

                            }
                            vm.criteriaChillaxingLength <= vm.chillaxingLengthInDay[day.date]?:0L -> {
                                textView.setTextColorRes(R.color.backgroundLight)
                                textView.setBackgroundResource(R.drawable.background_cat)
                                context?.let {
                                    textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it, R.color.dayStatusBlue))
                                }
                            }
                            vm.criteriaChillaxingLength * 0.8 <= vm.chillaxingLengthInDay[day.date]?:0L -> {
                                textView.setTextColorRes(R.color.backgroundLight)
                                textView.setBackgroundResource(R.drawable.background_cat)
                                context?.let {
                                    textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it, R.color.dayStatusGreen))
                                }
                            }
                            vm.criteriaChillaxingLength * 0.5 <= vm.chillaxingLengthInDay[day.date]?:0L -> {
                                textView.setTextColorRes(R.color.backgroundLight)
                                textView.setBackgroundResource(R.drawable.background_cat)
                                context?.let {
                                    textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it, R.color.dayStatusYellow))
                                }
                            }
                            vm.criteriaChillaxingLength * 0.3 <= vm.chillaxingLengthInDay[day.date]?:0L -> {
                                textView.setTextColorRes(R.color.backgroundLight)
                                textView.setBackgroundResource(R.drawable.background_cat)
                                context?.let {
                                    textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it, R.color.dayStatusOrange))
                                }
                            }
                            vm.criteriaChillaxingLength * 0.3 > vm.chillaxingLengthInDay[day.date]?:0L -> {
                                textView.setTextColorRes(R.color.backgroundLight)
                                textView.setBackgroundResource(R.drawable.background_cat)
                                context?.let {
                                    textView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(it, R.color.dayStatusRed))
                                }
                            }
                            else -> {}
                        }
                    }

                    // 기록이 있는 날짜를 표시하기 위함
                    if (binding.vm?.historicalDates?.contains(day.date) == true) {
                        val layoutParams = textView.layoutParams as ConstraintLayout.LayoutParams
                        layoutParams.setMargins(10.px)
                        textView.layoutParams = layoutParams
                        textView.setBackgroundResource(R.drawable.line_circle)
                    }
                    // 오늘 기준으로 미래 날짜에 대한 표현
                    if (LocalDate.now() < day.date) {
                        textView.setTextColorRes(R.color.calendarWeekdaysFuture)
                    } else { // 오늘을 포함하여 과거 날짜에 대한 표현
                        textView.setTextColorRes(R.color.calendarWeekdaysPast)
                    }

                    if (day.date.dayOfWeek == DayOfWeek.SATURDAY) {
                        textView.setTextColorRes(R.color.calendarSaturday)
                    } else if(day.date.dayOfWeek == DayOfWeek.SUNDAY || binding.vm?.holidaysMap?.contains(day.date) == true) {
                        textView.setTextColorRes(R.color.calendarSundayHoliday)
                    }

                    if (today == day.date) { // 오늘 날짜 체크 표시
                        textView.setTextColorRes(R.color.backgroundLight)

                        val layoutParams = textView.layoutParams as ConstraintLayout.LayoutParams
                        layoutParams.setMargins(10.px)
                        textView.layoutParams = layoutParams
                        textView.setBackgroundResource(R.drawable.button_square_round_corner)
                    }
                } else { // 선택한 달에 해당하지 않는 날짜들의 색상
                    textView.setTextColorRes(R.color.material_on_primary_disabled)
                    textView.background = null
                }
            }
        }

        binding.calendarviewCalendar.monthScrollListener = { calMonth ->
            // 해당하는 달의 데이터 새로고침
            val yyyymm = calMonth.yearMonth.toString().replace("-", "")
            binding.vm?.loadComponentInCalendar("${yyyymm.substring(0,4)}${yyyymm.substring(4,6)}", false)
//            logd("calendarMonth: $calMonth")
//            logd("yearMonth: ${calMonth.yearMonth.toString().replace("-", "")}")
//            logd("calendarMonth2: ${calMonth.month} ${calMonth.year} ${calMonth.yearMonth} ${calMonth.weekDays.size}")
            if (binding.calendarviewCalendar.maxRowCount == 6) {
                binding.tvCalendarCurrentYearText.text = calMonth.yearMonth.year.toString()
                binding.tvCalendarCurrentMonthText.text = monthTitleFormatter.format(calMonth.yearMonth)
            } else {
                val firstDate = calMonth.weekDays.first().first().date
                val lastDate = calMonth.weekDays.last().last().date
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