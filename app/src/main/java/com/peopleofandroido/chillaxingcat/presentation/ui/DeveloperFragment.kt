package com.peopleofandroido.chillaxingcat.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.FragmentDeveloperBinding

class DeveloperFragment : BaseBindingFragment<FragmentDeveloperBinding>() {
    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_developer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = this
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.developerToolbar.basicToolbarBack.setOnClickListener { findNavController().navigateUp() }
    }
}