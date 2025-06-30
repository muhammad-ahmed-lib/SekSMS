package com.simplemobiletools.smsmessenger.presentation.ui.screens


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.google.gson.Gson
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.simplemobiletools.commons.dialogs.RadioGroupDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.commons.models.RadioItem
import com.simplemobiletools.commons.models.SimpleContact
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.SimpleActivity
import com.simplemobiletools.smsmessenger.activities.ThreadActivity
import com.simplemobiletools.smsmessenger.adapters.ContactsAdapter
import com.simplemobiletools.smsmessenger.databinding.*
import com.simplemobiletools.smsmessenger.extensions.getSuggestedContacts
import com.simplemobiletools.smsmessenger.extensions.getThreadId
import com.simplemobiletools.smsmessenger.helpers.*
import com.simplemobiletools.smsmessenger.messaging.isShortCodeWithLetters
import com.simplemobiletools.smsmessenger.presentation.ui.adapters.GenericAdapter
import com.simplemobiletools.smsmessenger.presentation.ui.screens.main.MainActivity.DrawerModel
import com.simplemobiletools.smsmessenger.utils.loadImage
import java.net.URLDecoder
import java.util.Locale


data class SettingsModel(
    val title: String,
    val dsc: String,
    val icon: Int,
    val dscColor: Int = R.color.textGrayaaaaaa,
    val isSwitch: Boolean = true,
    val isSwitchOn: Boolean = false,
)

class SettingsActivity : SimpleActivity() {
    private var settingsList = ArrayList<SettingsModel>()

    private val binding by viewBinding(ActivityNewSettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = getColor(R.color.white)
        binding.backBtn.setOnClickListener {
            finish()
        }

        settingsList.add(
            SettingsModel(
                title = "Theme",
                dsc = "System default",
                icon = R.drawable.theme_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Font size",
                dsc = "Normal",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.font_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Use system font",
                dsc = "",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.user_system_font,
                isSwitch = true
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Language",
                dsc = "English",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.lang_ch_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Appearance",
                dsc = "",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.apprearence_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Chat Wallpaper",
                dsc = "",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.chat_wallpaper,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Notification",
                dsc = "",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.notification_set_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Delayed sending",
                dsc = "No delay",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.delayed_sending_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Swipe actions",
                dsc = "Configure swipe actions for conversations",
                icon = R.drawable.swip_action_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Swipe actions",
                dsc = "Configure swipe actions for conversations",
                icon = R.drawable.swip_action_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Post call screen",
                dsc = "Show call screen after call",
                icon = R.drawable.post_call_jcon,
                isSwitch = true
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Delete old messages automatically",
                dsc = "Never",
                dscColor = R.color.blueCommon0874f0,
                icon = R.drawable.delete_old_mes,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Delivery confirmations",
                dsc = "Confirm that messages were sent successfully",
                icon = R.drawable.delivery_confirmation_icon,
                isSwitch = true
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Signature",
                dsc = "Add a signature to the end of your messages",
                icon = R.drawable.signature_icon,
                isSwitch = false
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Strip accents",
                dsc = "Remove accents from characters in outgoing SMS Messages",
                icon = R.drawable.strip_accents_icon,
                isSwitch = true
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Mobile numbers only",
                dsc = "When composing a message, only show mobile numbers",
                icon = R.drawable.mobile_numbers_only,
                isSwitch = true
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Send long messages as MMS",
                dsc = "If your longer text messages are failing to send," +
                    "or sending in the wrong order, you can send " +
                    "them as MMS messages instead. Additional " +
                    "charges may apply",
                icon = R.drawable.send_long_message,
                isSwitch = true
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Auto compress MMS attachments",
                dsc = "When composing a message, only show mobile numbers",
                icon = R.drawable.auto_compress_icon,
                isSwitch = false,
                dscColor = R.color.blueCommon0874f0
            )
        )
        settingsList.add(
            SettingsModel(
                title = "Sync messages",
                dsc = "Re-sync your messages with the native " +
                    "Android SMS database",
                icon = R.drawable.sync_messages_icon,
                isSwitch = false,
            )
        )
        settingsList.add(
            SettingsModel(
                title = "About messages",
                dsc = "Version 1.0",
                icon = R.drawable.about_info_icon,
                dscColor = R.color.blueCommon0874f0,
                isSwitch = false,
            )
        )
        val adapter = GenericAdapter<SettingsModel, SettingsRecItemBinding>(
            items = settingsList,
            bindingInflater = SettingsRecItemBinding::inflate
        ) { binding, item ->  // Binding lambda
            binding.apply {
                icon.loadImage(item.icon)
                binding.titleTv.text=item.title
                if (item.dsc.isNotEmpty()){
                    binding.dsc.beVisible()
                    binding.dsc.text=item.dsc
                    binding.dsc.setTextColor(getColor(item.dscColor))
                }else{
                    binding.dsc.beGone()
                }
                if (item.isSwitch){
                    binding.swOnOff.beVisible()
                    binding.swOnOff.setContent {
                        Material3CustomSwitch()
                    }
                }else{
                    binding.swOnOff.beGone()
                }

            }
        }
        binding.rec.adapter=adapter
    }

    override fun onResume() {
        super.onResume()
    }

}
@Composable
fun Material3CustomSwitch() {
    var checked by remember { mutableStateOf(false) }

    androidx.compose.material3.Switch(
        checked = checked,
        onCheckedChange = { checked = it },
        colors = androidx.compose.material3.SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color.White,
            checkedTrackColor = Color(0xFF0874F0),
            uncheckedTrackColor = colorResource(R.color.textGrayaaaaaa)
        )
    )
}
