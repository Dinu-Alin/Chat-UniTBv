package com.lagar.chatunitbv.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ChatItemLayoutBinding
import com.lagar.chatunitbv.models.Chat
import com.lagar.chatunitbv.util.date.toCalendar
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import java.util.*

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

        val calendarTime = chat?.time?.toCalendar()

        "${calendarTime?.get(Calendar.HOUR_OF_DAY)}:${calendarTime?.get(Calendar.MINUTE)}:${
            calendarTime?.get(Calendar.SECOND)
        }".also { binding.chatLastAcessed.text = it }

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