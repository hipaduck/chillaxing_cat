package com.peopleofandroido.chillaxingcat.presentation

import android.app.AlarmManager
import android.app.NotificationManager
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.peopleofandroido.base.common.BaseBindingActivity
import com.peopleofandroido.base.common.NavManager
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.alarmManager
import com.peopleofandroido.chillaxingcat.databinding.ActivityMainBinding
import com.peopleofandroido.chillaxingcat.notificationManager
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

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager?
    }

    private fun initializeNavigation() {
        val navManager : NavManager = get()
        navManager.setOnNavEvent {
            navController.navigate(it)
        }
    }
}
