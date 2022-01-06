package com.lagar.chatunitbv.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.models.Chat

class MessagesActivity : AppCompatActivity() {

    private lateinit var chat: Chat
    val args: MessagesActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chat = args.selectedChat

        setContentView(R.layout.activity_messages)

    }

    fun getChat() = chat
}