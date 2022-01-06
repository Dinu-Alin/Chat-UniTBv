package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    @DocumentId
    val id: String? = null,
    val sender: String? = null,
    val content: String? = null,
    val chatId: String? = null,
    @ServerTimestamp
    val timestamp: Date? = null
)