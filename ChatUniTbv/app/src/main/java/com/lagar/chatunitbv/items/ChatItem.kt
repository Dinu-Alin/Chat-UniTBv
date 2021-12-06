package com.lagar.chatunitbv.items

import android.view.View
import coil.load
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ChatItemLayoutBinding
import com.lagar.chatunitbv.models.Chat
import com.xwray.groupie.viewbinding.BindableItem

class ChatItem(private val chat: Chat?) :
    BindableItem<ChatItemLayoutBinding>() {

    override fun bind(binding: ChatItemLayoutBinding, position: Int) {
        binding.chatLastAcessed.text = chat?.time ?: ""
        binding.chatName.text = chat?.name ?: "CHAT_NAME"
        binding.chatPhotoRv.load(chat?.imageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    override fun getLayout(): Int = R.layout.chat_item_layout

    override fun initializeViewBinding(view: View): ChatItemLayoutBinding =
        ChatItemLayoutBinding.bind(view)

    override fun equals(other: Any?): Boolean = when (other) {
        is ChatItem -> this.chat == other.chat
        else -> super.equals(other)
    }

    override fun hashCode(): Int {
        return chat?.hashCode() ?: 0
    }

}