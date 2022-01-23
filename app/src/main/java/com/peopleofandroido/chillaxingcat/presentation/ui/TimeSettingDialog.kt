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

        //event 를 setting vm 과 연결
        binding.timeSettingDialogWorkStartBtn.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    binding.timeSettingDialogWorkStartTv.text = "$hourOfDay : $minute"
                    //vm에서의 PREFERENCE 저장 로직
                },
                15,
                24,
                true
            )

            timePickerDialog.show()
        }

        binding.timeSettingDialogWorkFinishBtn.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    binding.timeSettingDialogWorkFinishTv.text = "$hourOfDay : $minute"
                    //vm에서의 PREFERENCE 저장 로직
                },
                15,
                24,
                true
            )

            timePickerDialog.show()
        }

        dialog.show()
    }

}