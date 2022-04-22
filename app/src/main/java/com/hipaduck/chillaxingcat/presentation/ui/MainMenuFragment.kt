package com.hipaduck.chillaxingcat.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.hipaduck.base.common.BaseBindingFragment
import com.hipaduck.chillaxingcat.R
import com.hipaduck.chillaxingcat.databinding.FragmentMainBinding
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
}