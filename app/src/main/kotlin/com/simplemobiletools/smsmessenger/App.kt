package com.simplemobiletools.smsmessenger

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.simplemobiletools.commons.extensions.checkUseEnglish
import com.simplemobiletools.smsmessenger.extensions.PreferencesUtil

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        PreferencesUtil.init(this)
        checkUseEnglish()
    }
}
