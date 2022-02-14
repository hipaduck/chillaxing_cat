package com.peopleofandroido.chillaxingcat.presentation.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.peopleofandroido.chillaxingcat.databinding.DialogDayRecordBinding

class DayDataDialog(private val context: Context, val binding: DialogDayRecordBinding) {
    private val dialog = BottomSheetDialog(context)

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.dismissWithAnimation = true

        binding.ivDialogDayRecordClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}