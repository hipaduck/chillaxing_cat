package com.peopleofandroido.chillaxingcat.presentation.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.DialogDayRecordBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DayDataDialog(private val context: Context, val binding: DialogDayRecordBinding, private val currentDate: LocalDate) {
    private val dialog = BottomSheetDialog(context)
    private var updateListener: (()->Unit)? = null

    fun setOnUpdateListener(listener: (()->Unit)?) {
        updateListener = listener
    }

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.dismissWithAnimation = true

        binding.vm?.let { vm ->
            if (vm.chillaxingLengthInDayMap.containsKey(currentDate)) {
                val lengthTimestamp = vm.chillaxingLengthInDayMap[currentDate] ?: 0L
                val lengthSeconds = lengthTimestamp / 1000L
                val lengthMinutes = lengthSeconds / 60
                val lengthHours = (lengthMinutes / 60).toInt()
                val lengthMinutesWithoutHours = (lengthMinutes % 60).toInt()

                binding.tvDialogDayRecordTitle.text =
                    String.format(context.getString(R.string.total_count_time_to_read), lengthHours.toString(), lengthMinutesWithoutHours.toString())
            } else {
                binding.tvDialogDayRecordTitle.text =
                    String.format(context.getString(R.string.total_count_time_to_read), "?", "?")
            }
            if (vm.holidaysMap.containsKey(currentDate)) {
                binding.tvDialogDayRecordExtraTitle.text = vm.holidaysMap[currentDate]
                binding.tvDialogDayRecordExtraTitle.visibility = View.VISIBLE
            } else {
                binding.tvDialogDayRecordExtraTitle.visibility = View.GONE
            }

            if (vm.chillaxingRecordInDayMap.containsKey(currentDate)) {
                // |로 데이터간의 간격 구별,  -로 시작 끝 구별
                val rawText = vm.chillaxingRecordInDayMap[currentDate]?:""
                val texts = rawText.split("|")
                var textForPrint = ""
                texts.forEach { text ->
                    val periodTexts = text.split("-")
                    if (periodTexts.size > 1) {
                        val start = SimpleDateFormat.getTimeInstance().format(Date(periodTexts[0].toLong()))
                        val end = SimpleDateFormat.getTimeInstance().format(Date(periodTexts[1].toLong()))
                        //LocalDate.parse("${startMonth}01", DateTimeFormatter.ofPattern("yyyyMMdd"))
                        textForPrint += "$start - $end\n"
                    }
                }
                binding.tvDialogDayRecordFullRecords.text = textForPrint
            }
        }

        binding.ivDialogDayRecordModify.setOnClickListener {
            binding.ivDialogDayRecordModify.visibility = View.GONE
            binding.tvDialogDayRecordTitle.visibility = View.GONE
            binding.llayoutDialogDayRecordModifyGroup.visibility = View.VISIBLE
            binding.ivDialogDayRecordSave.visibility = View.VISIBLE
        }

        binding.ivDialogDayRecordSave.setOnClickListener {
            binding.vm?.let { vm ->
                val hours = binding.etDialogDayRecordHoursInput.text.toString().toIntOrNull() ?: 0
                val minutes = binding.etDialogDayRecordMinutesInput.text.toString().toIntOrNull() ?: 0

                vm.storeSpecifiedDayRecord(currentDate, hours, minutes)

                binding.tvDialogDayRecordTitle.text =
                    String.format(context.getString(R.string.total_count_time_to_read), hours.toString(), minutes.toString())

                updateListener?.invoke()
            }
            binding.tvDialogDayRecordFullRecords.text = ""
            binding.ivDialogDayRecordModify.visibility = View.VISIBLE
            binding.tvDialogDayRecordTitle.visibility = View.VISIBLE
            binding.llayoutDialogDayRecordModifyGroup.visibility = View.GONE
            binding.ivDialogDayRecordSave.visibility = View.GONE
        }

        dialog.show()
    }

}