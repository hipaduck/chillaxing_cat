package com.hipaduck.base.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<T: ViewDataBinding> : AppCompatActivity() {
    @LayoutRes //layout resource value 를 return 하는 field 임을 의마하는 annotation
    abstract fun getLayoutResId(): Int

    protected lateinit var binding: T
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        //Databinding된 xml 내의 component들은 layout 파일의 resourceId 를 이용해 Layout을 찾는 과정을 거쳐야하지만,
        // binding만 사용하여 생략할 수 있도록 함
    }
}