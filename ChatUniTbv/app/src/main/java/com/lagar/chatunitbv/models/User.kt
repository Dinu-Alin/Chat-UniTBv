package com.lagar.chatunitbv.models

import com.google.firebase.firestore.DocumentId

data class User(
   @DocumentId
   val email: String? = null,
   val name: String? = null,
   val group: String? = null,
   val gender: String? = null
)

