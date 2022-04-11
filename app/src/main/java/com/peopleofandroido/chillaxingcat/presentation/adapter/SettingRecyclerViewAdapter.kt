package com.peopleofandroido.chillaxingcat.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.peopleofandroido.base.common.BaseBindingViewHolder
import com.peopleofandroido.chillaxingcat.R
import com.peopleofandroido.chillaxingcat.databinding.ItemSettingBinding
import com.peopleofandroido.chillaxingcat.domain.model.SettingMenuItemModel
import com.peopleofandroido.chillaxingcat.presentation.viewmodel.SettingViewModel

class SettingRecyclerViewAdapter (
    val vm : SettingViewModel,
    private val itemClicked : (Long?) -> Unit)
    : ListAdapter<SettingMenuItemModel, SettingRecyclerViewAdapter.SettingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingRecyclerViewAdapter.SettingViewHolder {
        return SettingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false), itemClicked)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        getItem(position)?.run {
            holder.binding.customTitle = ""
            holder.binding.item = this
            holder.binding.vm = vm
            holder.bind()
        }
    }

    inner class SettingViewHolder(itemView: View?, val itemClicked : (Long?) -> Unit) : BaseBindingViewHolder<ItemSettingBinding>(itemView!!) {
        fun bind () {
            binding.itemSettingLayout.setOnClickListener { itemClicked(binding.item!!.id) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SettingMenuItemModel>() {
            override fun areItemsTheSame(oldItem: SettingMenuItemModel, newItem: SettingMenuItemModel) = oldItem.title == newItem.title
            override fun areContentsTheSame(oldItem: SettingMenuItemModel, newItem: SettingMenuItemModel): Boolean = oldItem == newItem
        }
    }
}