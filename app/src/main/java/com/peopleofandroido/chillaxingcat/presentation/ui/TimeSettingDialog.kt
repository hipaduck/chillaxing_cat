package com.peopleofandroido.chillaxingcat.presentation.ui

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.peopleofandroido.chillaxingcat.databinding.DialogTimeSettingBinding

class TimeSettingDialog(private val context: Context, val binding: DialogTimeSettingBinding) {
    private val dialog = Dialog(context)

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)


        binding.vm?.let { vm ->
            binding.timeSettingDialogWorkStartTv.text = vm.workStartTime.value
            binding.timeSettingDialogWorkFinishTv.text = vm.workFinishTime.value

            val startList = vm.workStartTime.value.split(":")
            val finishList = vm.workFinishTime.value.split(":")
            var startHour = 9
            var startMinute = 0
            var finishHour = 18
            var finishMinute = 0

            if (startList.size == 2) {
                startHour = startList[0].toInt()
                startMinute = startList[1].toInt()
            }

            if (finishList.size == 2) {
                finishHour = finishList[0].toInt()
                finishMinute = finishList[1].toInt()
            }

            //event 를 setting vm 과 연결
            binding.timeSettingDialogWorkStartBtn.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        startHour = hourOfDay
                        startMinute = minute
                        val startDisplayTime = vm.changeToDisplayTime(startHour, startMinute)
                        binding.timeSettingDialogWorkStartTv.text = startDisplayTime

                        //vm에서의 PREFERENCE 저장 로직
                        vm.storeWorkStartTime(startDisplayTime)
                    },
                    startHour,
                    startMinute,
                    true
                )

                timePickerDialog.show()
            }

            binding.timeSettingDialogWorkFinishBtn.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        finishHour = hourOfDay
                        finishMinute = minute
                        binding.timeSettingDialogWorkFinishTv.text = vm.changeToDisplayTime(finishHour, finishMinute)
                        //vm에서의 PREFERENCE 저장 로직
                        vm.storeWorkFinishTime(hourOfDay, minute)
                    },
                    finishHour,
                    finishMinute,
                    true
                )

                timePickerDialog.show()
            }
        }

        binding.timeSettingDialogClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}