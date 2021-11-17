package com.lagar.chatunitbv.models

import com.google.firebase.database.Exclude

data class User(
    var name: String? = null,
    @Exclude var id: String? = null,
    var data: UserData? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name
        )
    }
}