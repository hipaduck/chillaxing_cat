package com.peopleofandroido.chillaxingcat.presentation.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.FragmentUserSettingBinding
import com.peopleofandroido.chillaxingcat.presentation.viewmodel.UserSettingViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class UserSettingFragment : BaseBindingFragment<FragmentUserSettingBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_user_setting
    private val userSettingNavArgs by navArgs<UserSettingFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingToolbar.basicToolbarBack.setOnClickListener { findNavController().navigateUp() }

        when (userSettingNavArgs.inputType) {
            "initial" -> {
                binding.settingToolbar.root.visibility = View.GONE
                binding.initializeToolbar.root.visibility = View.VISIBLE
                binding.initializeDialogConfirm.text = context?.getText(R.string.common_start)
                binding.vm?.isInitial = true
            }
            "setting" -> {
                binding.settingToolbar.root.visibility = View.VISIBLE
                binding.initializeToolbar.root.visibility = View.GONE
                binding.vm?.isInitial = false
            }
        }

        binding.vm?.let { vm ->
            binding.vm?.actionEvent?.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { action ->
                    when (action) {
                        is UserSettingViewModel.Action.DialogAction -> {
                            when (action.type) {
                                "show_time_dialog" -> {
                                    val reminderTimeHourMinute = vm.reminderTime.value.split(":")
                                    val timePickerDialog = TimePickerDialog(
                                        context,
                                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                            vm.reminderTime.value =
                                                vm.changeToDisplayTime(hourOfDay, minute)
                                        },
                                        Integer.parseInt(reminderTimeHourMinute[0]),
                                        Integer.parseInt(reminderTimeHourMinute[1]),
                                        true
                                    )
                                    timePickerDialog.show()
                                }

                                "pop_to_main" -> {
                                    findNavController().navigateUp()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}