package com.peopleofandroido.chillaxingcat.presentation.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imgRes")
fun imgLoad(imageView: ImageView, resId: Int) {
    imageView.setImageResource(resId)
}