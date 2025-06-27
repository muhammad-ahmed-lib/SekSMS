package com.simplemobiletools.smsmessenger.activities.root.appstate

import com.simplemobiletools.smsmessenger.R

open class BasicActivity : BaseRootActivity() {
    override fun getAppIconIDs() = arrayListOf(

        R.mipmap.ic_launcher,

    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)
}
