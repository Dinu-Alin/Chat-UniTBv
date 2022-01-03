package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId

data class Group(
    @DocumentId
    val name: String? = null,
    val specialisation: String? = null,
    val faculty: String? = null
)