package com.lagar.chatunitbv.firebase.autentification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Authenticator {
    val instance: FirebaseAuth = Firebase.auth
}