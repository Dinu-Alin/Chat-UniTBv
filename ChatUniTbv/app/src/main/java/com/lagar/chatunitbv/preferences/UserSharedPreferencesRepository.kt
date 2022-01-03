package com.lagar.chatunitbv.preferences

import android.content.SharedPreferences
import com.lagar.chatunitbv.models.User
import javax.inject.Inject

class UserSharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun write(user: User) {
        sharedPreferences.edit()
            .putString(USER_EMAIL, user.email)
            .putString(USER_NAME, user.name)
            .putString(USER_GROUP, user.group)
            .putString(USER_GENDER, user.gender)
            .apply()
    }

    fun read(): User {
        return User(
            email = sharedPreferences.getString(USER_EMAIL, null),
            name = sharedPreferences.getString(USER_NAME, null),
            group = sharedPreferences.getString(USER_GROUP, null),
            gender = sharedPreferences.getString(USER_GENDER, null)
        )
    }

    companion object {
        private const val USER_EMAIL = "USER_EMAIL"
        private const val USER_NAME = "USER_NAME"
        private const val USER_GROUP = "USER_GROUP"
        private const val USER_GENDER = "USER_GENDER"
    }
}