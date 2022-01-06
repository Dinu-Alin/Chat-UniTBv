package com.lagar.chatunitbv.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ChatItemLayoutBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.Chat
import com.lagar.chatunitbv.util.date.prettyPrintDate
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import io.github.rosariopfernandes.firecoil.load

class ChatItem(val chat: Chat?) :
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

        binding.chatLastAcessed.text = prettyPrintDate(chat?.timestamp)
        binding.chatName.text = chat?.name ?: ""
        if (chat!!.imageUrl == null) {
            chat.imageUrl = "images/chats/${chat.id}.png"
        }
        val reference = Operations.store.getReference(chat.imageUrl!!)

        binding.chatPhotoRv.load(reference) {
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