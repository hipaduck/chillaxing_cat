package com.peopleofandroido.chillaxingcat.presentation.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.DialogDayRecordBinding

class DayDataDialog(private val context: Context, val binding: DialogDayRecordBinding) {
    private val dialog = BottomSheetDialog(context)

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)
        dialog.dismissWithAnimation = true

        // todo: 휴식 데이터에서 가져온 값으로 반영해야 함
        binding.tvDialogDayRecordTitle.text =
            String.format(context.getString(R.string.total_count_time_to_read), "1", "10")

        // todo: 만약 holiday에 해당할 경우 해당되는 holiday의 name을 반영해야 함
        binding.tvDialogDayRecordExtraTitle.text = "창립기념일"

        // todo 만약 목록을 띄우는 것에 그칠 것이라면 하단의 데이터는 텍스트로 처리가능. 하지만 어떤 액션이 필요하다면 recyclerview 처리 필요

        binding.ivDialogDayRecordClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}