package com.lagar.chatunitbv.items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ChatItemLayoutBinding
import com.lagar.chatunitbv.models.Chat
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ChatItem(private val chat: Chat?) :
    AbstractBindingItem<ChatItemLayoutBinding>() {

    override val type: Int
        get() = R.id.chat_item

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ChatItemLayoutBinding {
        return ChatItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ChatItemLayoutBinding, payloads: List<Any>) {
        binding.chatLastAcessed.text = chat?.time ?: ""
        binding.chatName.text = chat?.name ?: ""

        binding.chatPhotoRv.load(chat?.imageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    override fun equals(other: Any?): Boolean = when (other) {
        is ChatItem -> this.chat == other.chat
        else -> super.equals(other)
    }

    override fun hashCode(): Int {
        return chat?.hashCode() ?: 0
    }
}