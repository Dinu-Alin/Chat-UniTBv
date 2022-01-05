package com.lagar.chatunitbv.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.SentMessageItemLayoutBinding
import com.lagar.chatunitbv.models.Message
import com.lagar.chatunitbv.util.date.prettyPrintDate
import com.mikepenz.fastadapter.binding.AbstractBindingItem

open class SentMessageItem(val message: Message?) :
    AbstractBindingItem<SentMessageItemLayoutBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SentMessageItemLayoutBinding {
        return SentMessageItemLayoutBinding.inflate(inflater, parent, false)
    }

    override val type: Int = R.id.sent_message_full

    override fun bindView(binding: SentMessageItemLayoutBinding, payloads: List<Any>) {

        binding.sentMessageBody.text = message?.content ?: ""
        binding.sentMessageDate.text = prettyPrintDate(message?.timestamp)
    }
}