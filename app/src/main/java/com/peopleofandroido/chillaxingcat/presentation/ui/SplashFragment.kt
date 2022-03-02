package com.peopleofandroido.chillaxingcat.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.peopleofandroido.base.common.BaseBindingFragment
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.FragmentSplashBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*
import kotlin.coroutines.CoroutineContext

class SplashFragment : BaseBindingFragment<FragmentSplashBinding>(), CoroutineScope {
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_splash

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = getViewModel()
        binding.lifecycleOwner = this
        job = Job()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Locale.getDefault().language == "ko") {
            binding.splashFragmentImage.setImageResource(R.drawable.splash_cat_sub_korean)
        } else {
            binding.splashFragmentImage.setImageResource(R.drawable.splash_cat_sub_eng)
        }

        launch {
            withContext(Dispatchers.Main) {
                delay(1200)
                binding.vm?.moveToMain()
            }

        }
    }
}