package com.example.sharedoc.epoxy_controllers

import com.airbnb.epoxy.TypedEpoxyController
import com.example.sharedoc.R
import com.example.sharedoc.UserDto
import com.example.sharedoc.ViewBindingKotlinModel
import com.example.sharedoc.databinding.UserListItemBinding

class UsersEpoxyController(
private val itemClickListener: OnItemClickListener
) : TypedEpoxyController<List<UserDto>>() {

    interface OnItemClickListener {
        fun onItemClick(item: UserDto)
    }

    override fun buildModels(data: List<UserDto>?) {
        if (data.isNullOrEmpty()) {
            return
        }
        data.forEach {
            ChannelEpoxyModel(it, itemClickListener).id(1).addTo(this)
        }
    }

    data class ChannelEpoxyModel(val item: UserDto,
                                 private val itemClickListener: OnItemClickListener,
    ) : ViewBindingKotlinModel<UserListItemBinding>(R.layout.user_list_item) {

        override fun UserListItemBinding.bind() {

//            Glide.with(channelImage)
//                .load(item.snippet.thumbnails.high.url)
//                .into(channelImage)
//
//            channelName.text = item.snippet.channelTitle
//
//            subscribeButton.setOnClickListener {
//                itemClickListener.onItemClick(item)
//            }

        }
    }

}