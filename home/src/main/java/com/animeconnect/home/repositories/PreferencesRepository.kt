package com.animeconnect.home.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity

class PreferencesRepository(activity: FragmentActivity) {
    private val preferences: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    companion object {
        const val ACCESS_TOKEN = "access_token"
    }

    fun getAccessToken(): String? = preferences.getString(ACCESS_TOKEN, "")

    fun saveAccessToken(accessToken: String) {
        preferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .apply()
    }
}