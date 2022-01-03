package com.lagar.chatunitbv.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object Operations {

    val db by lazy { Firebase.firestore }
    val store by lazy { Firebase.storage }
    val auth by lazy { Firebase.auth }
}