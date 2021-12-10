package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    @DocumentId
    val id: String? = null,
    val sender: String? = null,
    var content: String? = null,
    @ServerTimestamp
    var timestamp: Date? = null
)