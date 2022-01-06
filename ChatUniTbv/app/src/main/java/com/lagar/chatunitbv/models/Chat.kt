package com.lagar.chatunitbv.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Chat(
    @DocumentId
    val id: String? = null,
    var name: String? = null,
    var members: List<String?>? = null,
    var memberCount: Int? = null,
    var timestamp: Date? = null,
    var image: String? = null
) : Parcelable