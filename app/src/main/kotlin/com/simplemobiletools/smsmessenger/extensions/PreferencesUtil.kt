package com.simplemobiletools.smsmessenger.extensions

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesUtil private constructor(context: Context) {

    companion object {
        @Volatile
        private var instance: PreferencesUtil? = null
        private lateinit var sharedPreferences: SharedPreferences

        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PreferencesUtil(context.applicationContext)
                    }
                }
            }
        }

        fun getInstance(): PreferencesUtil {
            check(::sharedPreferences.isInitialized) { "PreferencesUtil is not initialized. Call init(context) first." }
            return instance!!
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            "mobitel_preferences", Context.MODE_PRIVATE
        )
    }

    fun getFloatValue(key: String?, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun setFloatValue(key: String?, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun getLongValue(key: String?, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun setLongValue(key: String?, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun setIntValue(key: String?, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIntValue(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun setArrayListValue(key: String?, list: List<String>) {
        val gson = Gson()
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getArrayListValue(key: String?, defValue: String?): List<String> {
        val gson = Gson()
        val json: String? = sharedPreferences.getString(key, defValue)
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun setStringValue(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, null)
    }

    fun getStringValue(key: String?, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun getBooleanValue(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun setBooleanValue(key: String?, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }

    fun clearPreference(key: String?) {
        sharedPreferences.edit().remove(key).apply()
    }
}
