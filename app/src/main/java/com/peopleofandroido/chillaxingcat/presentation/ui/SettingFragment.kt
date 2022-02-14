package com.peopleofandroido.chillaxingcat.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.DialogCatJobBinding
import com.peopleofandroido.chillaxingcat.databinding.DialogTimeSettingBinding
import com.peopleofandroido.chillaxingcat.databinding.FragmentSettingBinding
import com.peopleofandroido.chillaxingcat.presentation.viewmodel.SettingViewModel
import org.koin.android.compat.ScopeCompat.getViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingFragment : BaseBindingFragment<FragmentSettingBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_setting

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

        binding.vm?.actionEvent?.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { action ->
                when (action) {
                    is SettingViewModel.Action.DialogAction -> {
                        when (action.type) {
                            "setting_time_setting_dialog" -> {
                                val dialogBinding = DataBindingUtil.inflate<DialogTimeSettingBinding>(
                                    LayoutInflater.from(context), R.layout.dialog_time_setting, null, false)
                                dialogBinding.vm = binding.vm
                                val dialog = TimeSettingDialog(requireContext(), dialogBinding)
                                dialog.show()
                            }
                            "setting_job_setting_dialog" -> {
                                val dialogBinding = DataBindingUtil.inflate<DialogCatJobBinding>(
                                    LayoutInflater.from(context), R.layout.dialog_cat_job, null, false)
                                dialogBinding.vm = binding.vm
                                val dialog = JobSettingDialog(requireContext(), dialogBinding)
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }
    }
}