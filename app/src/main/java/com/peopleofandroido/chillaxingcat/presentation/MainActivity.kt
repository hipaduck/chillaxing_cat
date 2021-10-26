package com.peopleofandroido.chillaxingcat.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import com.peopleofandroido.base.common.BaseBindingActivity
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    @LayoutRes
    override fun getLayoutResId() =
        R.layout.activity_main

    private val navController
        get() = nav_host.findNavController()

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
