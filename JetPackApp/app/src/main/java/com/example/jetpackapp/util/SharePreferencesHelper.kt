package com.example.jetpackapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharePreferencesHelper {

    companion object {
        private const val PREF_TIME = "TIME"
        private var pref: SharedPreferences? = null

        @Volatile
        private var instance: SharePreferencesHelper? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildSharePreference(context).also {
                instance = it
            }
        }

        private fun buildSharePreference(context: Context): SharePreferencesHelper {
            pref = PreferenceManager.getDefaultSharedPreferences(context)
            return SharePreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        pref?.edit(commit = true) {
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime(): Long? {
        return pref?.getLong(PREF_TIME, 0)
    }
}