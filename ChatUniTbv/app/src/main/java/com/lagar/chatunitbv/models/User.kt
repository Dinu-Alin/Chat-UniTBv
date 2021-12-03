package com.lagar.chatunitbv.models

data class User(
    var name: String? = null,
    var id: String? = null,
    var data: UserData? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name
        )
    }
}