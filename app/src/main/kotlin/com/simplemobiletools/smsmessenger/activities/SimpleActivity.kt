package com.simplemobiletools.smsmessenger.activities

import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.root.appstate.BaseSimpleCopy

open class SimpleActivity : BaseSimpleCopy() {
    override fun getAppIconIDs() = arrayListOf(

        R.mipmap.ic_launcher,

    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)
}
