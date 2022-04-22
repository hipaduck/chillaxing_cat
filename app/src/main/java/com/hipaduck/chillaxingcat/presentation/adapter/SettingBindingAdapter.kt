package com.hipaduck.chillaxingcat.presentation.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hipaduck.chillaxingcat.domain.model.SettingMenuItemModel
import com.hipaduck.chillaxingcat.presentation.viewmodel.SettingViewModel

@BindingAdapter(value = ["settingList", "view"])
fun setSettingItems (view : RecyclerView, settingList : List<SettingMenuItemModel>, viewModel: SettingViewModel) {
    view.adapter?.run {
        if (this is SettingRecyclerViewAdapter) {
            this.submitList(settingList)
            this.notifyDataSetChanged()
        }
    } ?: run {
        SettingRecyclerViewAdapter(viewModel) { item ->
            viewModel.processMenu(item)
        }.apply {
            view.adapter = this
            this.submitList(settingList)
        }
    }
}