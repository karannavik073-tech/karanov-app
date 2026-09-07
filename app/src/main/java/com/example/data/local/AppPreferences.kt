package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("karanov_ai_prefs", Context.MODE_PRIVATE)

    var selectedLanguageCode: String
        get() = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var isRealTimeSearchEnabled: Boolean
        get() = prefs.getBoolean(KEY_REAL_TIME, true)
        set(value) = prefs.edit().putBoolean(KEY_REAL_TIME, value).apply()

    var customApiKey: String
        get() = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_CUSTOM_API_KEY, value).apply()

    var autoSpeakResponses: Boolean
        get() = prefs.getBoolean(KEY_AUTO_SPEAK, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_SPEAK, value).apply()

    var currentSessionId: String?
        get() = prefs.getString(KEY_CURRENT_SESSION, null)
        set(value) = prefs.edit().putString(KEY_CURRENT_SESSION, value).apply()

    companion object {
        private const val KEY_LANGUAGE = "selected_language"
        private const val KEY_REAL_TIME = "real_time_search_enabled"
        private const val KEY_CUSTOM_API_KEY = "custom_api_key"
        private const val KEY_AUTO_SPEAK = "auto_speak_responses"
        private const val KEY_CURRENT_SESSION = "current_session_id"
    }
}
