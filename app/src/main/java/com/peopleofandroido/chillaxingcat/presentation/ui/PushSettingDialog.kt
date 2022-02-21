package com.peopleofandroido.chillaxingcat.presentation.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.peopleofandroido.chillaxingcat.databinding.DialogPushSettingBinding

class PushSettingDialog(private val context: Context, val binding: DialogPushSettingBinding) {
    private val dialog = Dialog(context)

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)

        binding.vm?.let { vm ->
            binding.settingPushToggle.isChecked = vm.pushAvailable.value

            binding.settingPushConfirmIv.setOnClickListener {
                vm.storePushSetting(binding.settingPushToggle.isChecked)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}