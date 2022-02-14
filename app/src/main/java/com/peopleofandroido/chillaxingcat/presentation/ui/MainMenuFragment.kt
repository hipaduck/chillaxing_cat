package com.peopleofandroido.chillaxingcat.presentation.ui

import InitializeSettingDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.DialogInitialSettingBinding
import com.peopleofandroido.chillaxingcat.databinding.DialogTimeSettingBinding
import com.peopleofandroido.chillaxingcat.databinding.FragmentMainBinding
import com.peopleofandroido.chillaxingcat.presentation.viewmodel.MainMenuViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainMenuFragment : BaseBindingFragment<FragmentMainBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_main

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
                    is MainMenuViewModel.Action.DialogAction -> {
                        when (action.type) {
                            "main_time_setting_dialog" -> {
                                val dialogBinding = DataBindingUtil.inflate<DialogInitialSettingBinding>(
                                    LayoutInflater.from(context), R.layout.dialog_initial_setting, null, false)
                                dialogBinding.vm = binding.vm
                                val dialog = InitializeSettingDialog(requireContext(), dialogBinding)
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }
    }
}