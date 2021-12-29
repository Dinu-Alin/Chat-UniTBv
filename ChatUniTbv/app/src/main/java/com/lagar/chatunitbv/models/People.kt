package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference

data class People(
    @DocumentId
    val id: String? = null,
    var gender: String? = null,
    var group: DocumentReference? = null,
    var name: String? = null
)