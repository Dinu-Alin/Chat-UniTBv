package com.lagar.chatunitbv.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ReceivedMessageItemLayoutBinding
import com.lagar.chatunitbv.models.Message
import com.lagar.chatunitbv.util.date.prettyPrintDate
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ReceivedMessageItem(val message: Message?) :
    AbstractBindingItem<ReceivedMessageItemLayoutBinding>(){
    override val type: Int = R.id.received_message_full

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ReceivedMessageItemLayoutBinding {
        return ReceivedMessageItemLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ReceivedMessageItemLayoutBinding, payloads: List<Any>) {
        binding.receivedMessageBody.text = message?.content ?: ""
        binding.receivedMessageDate.text = prettyPrintDate(message?.timestamp)
        binding.receivedMessageSender.text = message?.sender
    }

}