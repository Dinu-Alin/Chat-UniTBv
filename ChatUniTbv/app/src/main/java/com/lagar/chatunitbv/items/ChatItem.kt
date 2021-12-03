package com.lagar.chatunitbv.items

import android.view.View
import coil.load
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ChatItemLayoutBinding
import com.xwray.groupie.viewbinding.BindableItem

class ChatItem(var imageUrl: String, var chatName: String, var time: String) :
    BindableItem<ChatItemLayoutBinding>() {

    override fun bind(binding: ChatItemLayoutBinding, position: Int) {
        binding.chatLastAcessed.text = time
        binding.chatName.text = chatName
        binding.chatPhotoRv.load(imageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    override fun getLayout(): Int = R.layout.chat_item_layout

    override fun initializeViewBinding(view: View): ChatItemLayoutBinding =
        ChatItemLayoutBinding.bind(view)

}