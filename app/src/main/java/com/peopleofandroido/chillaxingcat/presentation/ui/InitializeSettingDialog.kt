import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.peopleofandroido.chillaxingcat.databinding.DialogInitialSettingBinding

class InitializeSettingDialog(private val context: Context, val binding: DialogInitialSettingBinding) {
    private val dialog = Dialog(context)

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(binding.root)

        var startHour: Int = 9
        var startMinute: Int = 0

        var finishHour : Int = 18
        var finishMinute : Int = 0

        binding.vm?.let { vm ->
            binding.intializeDialogWorkStartTv.text = "09:00"
            binding.intializeDialogWorkFinishTv.text = "18:00"

            //event 를 setting vm 과 연결
            binding.intializeDialogWorkStartBtn.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        startHour = hourOfDay
                        startMinute = minute
                        val startDisplayTime = changeToDisplayTime(startHour, startMinute)
                        binding.intializeDialogWorkStartTv.text = startDisplayTime
                    },
                    startHour,
                    startMinute,
                    true
                )

                timePickerDialog.show()
            }

            binding.intializeDialogWorkFinishBtn.setOnClickListener {
                val timePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        finishHour = hourOfDay
                        finishMinute = minute
                        binding.intializeDialogWorkFinishTv.text = changeToDisplayTime(finishHour, finishMinute)
                    },
                    finishHour,
                    finishMinute,
                    true
                )

                timePickerDialog.show()
            }
        }

        binding.intializeDialogConfirm.setOnClickListener {
            //vm에서의 PREFERENCE 저장 로직
            binding.vm?.storeWorkStartTime(changeToDisplayTime(startHour, startMinute))
            binding.vm?.storeWorkFinishTime(finishHour, finishMinute)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    private fun changeToDisplayTime(hour: Int, minute: Int) : String {
        return String.format("%02d:%02d", hour, minute)
    }
}