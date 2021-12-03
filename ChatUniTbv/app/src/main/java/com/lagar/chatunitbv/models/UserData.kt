package com.lagar.chatunitbv.models

data class UserData(
    val group: String? = null,
    val field: String? = null,
    val photoUrl: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "group" to group,
            "field" to field,
            "photoUrl" to photoUrl
        )
    }
}