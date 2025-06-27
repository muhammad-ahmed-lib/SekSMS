package com.simplemobiletools.smsmessenger.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TinyDB(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("TinyDB", Context.MODE_PRIVATE)
    private val TOKEN="TOKEN"
    private val OWNER_TOKEN="OWNER_TOKEN"


    fun putToken(value: Boolean)=putBoolean(TOKEN,value)
    fun getToken()=getBoolean(TOKEN)
    fun putOwnerToken(value: String)=putString(OWNER_TOKEN,value)
    fun getOwnerToken()=getString(OWNER_TOKEN)
    fun putString(key: String, value: String) {
        preferences.edit() { putString(key, value) }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        preferences.edit() { putInt(key, value) }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit() { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        preferences.edit() { putFloat(key, value) }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit() { putLong(key, value) }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return preferences.getLong(key, defaultValue)
    }




    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun clear() {
        preferences.edit() { clear() }
    }
}
