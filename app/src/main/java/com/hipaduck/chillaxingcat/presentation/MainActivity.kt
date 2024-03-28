package com.hipaduck.chillaxingcat.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.navigation.findNavController
import com.hipaduck.base.common.BaseBindingActivity
import com.hipaduck.base.common.NavManager
import com.hipaduck.chillaxingcat.R
import com.hipaduck.chillaxingcat.databinding.ActivityMainBinding
import org.koin.android.ext.android.get

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    @LayoutRes
    override fun getLayoutResId() =
        R.layout.activity_main

    private val navController
        get() = binding.navHost.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeNavigation()
        binding.lifecycleOwner = this
    }

    private fun initializeNavigation() {
        val navManager : NavManager = get()
        navManager.setOnNavEvent {
            navController.navigate(it)
        }
    }
}
