package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Chat(
    @DocumentId
    val id: String? = null,
    var imageUrl: String? = null,
    var name: String? = null,
    var time: Date? = null
)