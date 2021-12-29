package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId

data class Group(
    @DocumentId
    val id: String? = null,
    var faculty: String? = null,
    var specialisation: String? = null,
)