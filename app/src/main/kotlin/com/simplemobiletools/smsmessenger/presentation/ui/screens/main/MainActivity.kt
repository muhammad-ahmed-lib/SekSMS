package com.simplemobiletools.smsmessenger.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.provider.Telephony
import android.text.TextUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.presentation.ui.RateUsFragment
import com.simplemobiletools.commons.dialogs.PermissionRequiredDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.extensions.hideKeyboard
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.commons.models.FAQItem
import com.simplemobiletools.commons.models.Release
import com.simplemobiletools.smsmessenger.BuildConfig
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.ArchivedConversationsActivity
import com.simplemobiletools.smsmessenger.activities.NewConversationActivity
import com.simplemobiletools.smsmessenger.activities.RecycleBinConversationsActivity
import com.simplemobiletools.smsmessenger.activities.SettingsActivity
import com.simplemobiletools.smsmessenger.activities.ThreadActivity
import com.simplemobiletools.smsmessenger.activities.root.appstate.BasicActivity
import com.simplemobiletools.smsmessenger.activities.root.appstate.checkWhatsNew
import com.simplemobiletools.smsmessenger.adapters.ConversationsAdapter
import com.simplemobiletools.smsmessenger.adapters.SearchResultsAdapter
import com.simplemobiletools.smsmessenger.databinding.ActivityMainBinding
import com.simplemobiletools.smsmessenger.databinding.DrawerRecItemBinding
import com.simplemobiletools.smsmessenger.extensions.*
import com.simplemobiletools.smsmessenger.helpers.*
import com.simplemobiletools.smsmessenger.models.Conversation
import com.simplemobiletools.smsmessenger.models.Events
import com.simplemobiletools.smsmessenger.models.Message
import com.simplemobiletools.smsmessenger.models.SearchResult
import com.simplemobiletools.smsmessenger.presentation.ui.adapters.GenericAdapter
import com.simplemobiletools.smsmessenger.presentation.ui.screens.SearchConversationActivity
import com.simplemobiletools.smsmessenger.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BasicActivity() {
    private val MAKE_DEFAULT_APP_REQUEST = 1

    private var storedTextColor = 0
    private var storedFontSize = 0
    private var lastSearchedText = ""
    private var bus: EventBus? = null
    private var wasProtectionHandled = false
    private var permissionsGranted = false

    private val binding by viewBinding(ActivityMainBinding::inflate)
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appLaunched(BuildConfig.APPLICATION_ID)

        TinyDB(this).putToken(true)
        drawer()
        setupOptionsMenu()
        refreshMenuItems()
        binding.permissionBtn.setOnClickListener {
            requestSmsPermissions()
        }
      /*  updateMaterialActivityViews(
            mainCoordinatorLayout = null,
            nestedView = binding.rec,
            useTransparentNavigation = true,
            useTopSearchMenu = true
        )
*/


        binding.searchBtn.setOnClickListener {
            openActivity<SearchConversationActivity>()
        }
        checkPermissions()

        if (savedInstanceState == null) {
            checkAndDeleteOldRecycleBinMessages()
            handleAppPasswordProtection {
                wasProtectionHandled = it
                if (it) {
                    clearAllMessagesIfNeeded {
                        if (!permissionsGranted) {
                            showPermissionButton()
                        }
                    }
                } else {
                    finish()
                }
            }
        }

        if (checkAppSideloading()) {
            return
        }
    }

    private fun checkPermissions() {
        permissionsGranted = hasPermission(PERMISSION_READ_SMS) &&
            hasPermission(PERMISSION_SEND_SMS) &&
            hasPermission(PERMISSION_READ_CONTACTS)

        if (permissionsGranted) {
            hidePermissionButton()
            initMessenger()
            bus = EventBus.getDefault()
            try {
                bus!!.register(this)
            } catch (ignored: Exception) {
            }
        } else {
            showPermissionButton()
        }
    }

    private fun drawer(){
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.menuBtn.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        val toolsList= listOf(
            DrawerModel(
                title = "Inbox",
                icon = R.drawable.inbox_icon
            ),
            DrawerModel(
                title = "Archived",
                icon = R.drawable.archived_icon
            ),
            DrawerModel(
                title = "Unread",
                icon = R.drawable.unread_messages_icon
            ),
            DrawerModel(
                title = "Draft",
                icon = R.drawable.draft_messages_icon
            ),
            DrawerModel(
                title = "Private chat",
                icon = R.drawable.private_chat_icon
            ),
            DrawerModel(
                title = "Stranger",
                icon = R.drawable.stranger_icon
            ),
            DrawerModel(
                title = "Spam and blocked",
                icon = R.drawable.spam_blocked_icon
            ),
            DrawerModel(
                title = "Scheduled",
                icon = R.drawable.scheduled_icon
            ),
            DrawerModel(
                title = "Backup and restore",
                icon = R.drawable.backup_restore_icon
            ),
            DrawerModel(
                title = "Setting",
                icon = R.drawable.settings_icon
            )
        )

        val adapter = GenericAdapter<DrawerModel, DrawerRecItemBinding>(
            items = toolsList,
            bindingInflater = DrawerRecItemBinding::inflate
        ) { binding, item ->  // Binding lambda
            binding.apply {
                icon.loadImage(item.icon)
                binding.titleTv.text=item.title
                root.setOnClickListener {
                    when(item.icon){
                        R.drawable.archived_icon-> launchArchivedConversations()
                    }
                }
            }
        }
        binding.drawerInclude.toolsRec.adapter=adapter

        val list= listOf(
            DrawerModel(
                title = "Share",
                icon = R.drawable.share_icon
            ),
            DrawerModel(
                title = "Rate App",
                icon = R.drawable.rate_us_icon
            ),
            DrawerModel(
                title = "Privacy Policy",
                icon = R.drawable.private_chat_icon
            )
        )

        val adapte = GenericAdapter<DrawerModel, DrawerRecItemBinding>(
            items = list,
            bindingInflater = DrawerRecItemBinding::inflate
        ) { binding, item ->  // Binding lambda
            binding.apply {
                icon.loadImage(item.icon)
                binding.titleTv.text=item.title
                root.setOnClickListener {
                    when(item.icon){
                        R.drawable.share_icon-> shareApp()
                        R.drawable.rate_us_icon-> rateUs()
                        R.drawable.private_chat_icon-> openWebLink()
                    }
                }
            }
        }
        binding.drawerInclude.rec.adapter=adapte

    }
    data class DrawerModel(
        val title:String="",
        val icon:Int=0,
        val screen:Class<*>?=null
    )
    override fun onResume() {
        super.onResume()
        updateMenuColors()
        refreshMenuItems()

        if (permissionsGranted) {
            getOrCreateConversationsAdapter().apply {
                if (storedTextColor != getProperTextColor()) {
                    updateTextColor(getProperTextColor())
                }

                if (storedFontSize != config.fontSize) {
                    updateFontSize()
                }

                updateDrafts()
            }

          //  updateTextColors(binding.mainCoordinator)
            binding.searchHolder.setBackgroundColor(getProperBackgroundColor())

            val properPrimaryColor = getProperPrimaryColor()
            binding.noConversationsPlaceholder2.setTextColor(properPrimaryColor)
            binding.noConversationsPlaceholder2.underlineText()
            binding.conversationsFastscroller.updateColors(properPrimaryColor)
            binding.conversationsProgressBar.setIndicatorColor(properPrimaryColor)
            binding.conversationsProgressBar.trackColor = properPrimaryColor.adjustAlpha(LOWER_ALPHA)
            checkShortcut()
        }
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus?.unregister(this)
    }

    override fun onBackPressed() {
        if (binding.mainMenu.isSearchOpen) {
            binding.mainMenu.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(WAS_PROTECTION_HANDLED, wasProtectionHandled)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        wasProtectionHandled = savedInstanceState.getBoolean(WAS_PROTECTION_HANDLED, false)

        if (!wasProtectionHandled) {
            handleAppPasswordProtection {
                wasProtectionHandled = it
                if (it) {
                    if (!permissionsGranted) {
                        showPermissionButton()
                    }
                } else {
                    finish()
                }
            }
        }
    }

    private fun showPermissionButton() {
        binding.permissionLayout.beVisible()
    }

    private fun hidePermissionButton() {
        binding.permissionLayout.beGone()
    }

    private fun requestSmsPermissions() {
        if (isQPlus()) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager!!.isRoleAvailable(RoleManager.ROLE_SMS)) {
                if (roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                    askPermissions()
                } else {
                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
                    startActivityForResult(intent, MAKE_DEFAULT_APP_REQUEST)
                }
            } else {
                toast(com.simplemobiletools.commons.R.string.unknown_error_occurred)
            }
        } else {
            if (Telephony.Sms.getDefaultSmsPackage(this) == packageName) {
                askPermissions()
            } else {
                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                startActivityForResult(intent, MAKE_DEFAULT_APP_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == MAKE_DEFAULT_APP_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                askPermissions()
            } else {
                showPermissionButton()
            }
        }
    }

    private fun askPermissions() {
        handlePermission(PERMISSION_READ_SMS) {
            if (it) {
                handlePermission(PERMISSION_SEND_SMS) {
                    if (it) {
                        handlePermission(PERMISSION_READ_CONTACTS) {
                            handleNotificationPermission { granted ->
                                if (!granted) {
                                    PermissionRequiredDialog(
                                        activity = this,
                                        textId = com.simplemobiletools.commons.R.string.allow_notifications_incoming_messages,
                                        positiveActionCallback = { openNotificationSettings() })
                                }
                            }

                            permissionsGranted = true
                            hidePermissionButton()
                            initMessenger()
                            bus = EventBus.getDefault()
                            try {
                                bus!!.register(this)
                            } catch (ignored: Exception) {
                            }
                        }
                    } else {
                       // toast(R.string.sms_permission_required)
                    }
                }
            } else {
              //  toast(R.string.sms_permission_required)
            }
        }
    }

    private fun setupOptionsMenu() {
        binding.mainMenu.getToolbar().inflateMenu(R.menu.menu_main)
        binding.mainMenu.toggleHideOnScroll(true)
        binding.mainMenu.setupMenu()

        binding.mainMenu.onSearchClosedListener = {
            fadeOutSearch()
        }

        binding.mainMenu.onSearchTextChangedListener = { text ->
            if (text.isNotEmpty()) {
                if (binding.searchHolder.alpha < 1f) {
                    binding.searchHolder.fadeIn()
                }
            } else {
                fadeOutSearch()
            }
            searchTextChanged(text)
        }


        binding.mainMenu.getToolbar().setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.more_apps_from_us -> launchMoreAppsFromUsIntent()
                R.id.show_recycle_bin -> launchRecycleBin()
                R.id.show_archived -> launchArchivedConversations()
                R.id.settings -> launchSettings()
                R.id.about -> launchAbout()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun refreshMenuItems() {
        binding.mainMenu.getToolbar().menu.apply {
            findItem(R.id.more_apps_from_us).isVisible = !resources.getBoolean(com.simplemobiletools.commons.R.bool.hide_google_relations)
            findItem(R.id.show_recycle_bin).isVisible = config.useRecycleBin
            findItem(R.id.show_archived).isVisible = config.isArchiveAvailable
        }
    }

    private fun storeStateVariables() {
        storedTextColor = getProperTextColor()
        storedFontSize = config.fontSize
    }

    private fun updateMenuColors() {
        updateStatusbarColor(getProperBackgroundColor())
        binding.mainMenu.updateColors()
    }

    private fun initMessenger() {
        checkWhatsNewDialog()
        storeStateVariables()
        getCachedConversations()

        binding.noConversationsPlaceholder2.setOnClickListener {
            launchNewConversation()
        }

        binding.conversationsFab.setOnClickListener {
            launchNewConversation()
        }
    }

    private fun getCachedConversations() {
        ensureBackgroundThread {
            val conversations = try {
                conversationsDB.getNonArchived().toMutableList() as ArrayList<Conversation>
            } catch (e: Exception) {
                ArrayList()
            }

            val archived = try {
                conversationsDB.getAllArchived()
            } catch (e: Exception) {
                listOf()
            }

            updateUnreadCountBadge(conversations)
            runOnUiThread {
                setupConversations(conversations, cached = true)
                getNewConversations((conversations + archived).toMutableList() as ArrayList<Conversation>)
            }
            conversations.forEach {
                clearExpiredScheduledMessages(it.threadId)
            }
        }
    }

    private fun getNewConversations(cachedConversations: ArrayList<Conversation>) {
        val privateCursor = getMyContactsCursor(favoritesOnly = false, withPhoneNumbersOnly = true)
        ensureBackgroundThread {
            val privateContacts = MyContactsContentProvider.getSimpleContacts(this, privateCursor)
            val conversations = getConversations(privateContacts = privateContacts)

            conversations.forEach { clonedConversation ->
                val threadIds = cachedConversations.map { it.threadId }
                if (!threadIds.contains(clonedConversation.threadId)) {
                    conversationsDB.insertOrUpdate(clonedConversation)
                    cachedConversations.add(clonedConversation)
                }
            }

            cachedConversations.forEach { cachedConversation ->
                val threadId = cachedConversation.threadId

                val isTemporaryThread = cachedConversation.isScheduled
                val isConversationDeleted = !conversations.map { it.threadId }.contains(threadId)
                if (isConversationDeleted && !isTemporaryThread) {
                    conversationsDB.deleteThreadId(threadId)
                }

                val newConversation = conversations.find { it.phoneNumber == cachedConversation.phoneNumber }
                if (isTemporaryThread && newConversation != null) {
                    conversationsDB.deleteThreadId(threadId)
                    messagesDB.getScheduledThreadMessages(threadId)
                        .forEach { message ->
                            messagesDB.insertOrUpdate(message.copy(threadId = newConversation.threadId))
                        }
                    insertOrUpdateConversation(newConversation, cachedConversation)
                }
            }

            cachedConversations.forEach { cachedConv ->
                val conv = conversations.find {
                    it.threadId == cachedConv.threadId && !Conversation.areContentsTheSame(cachedConv, it)
                }
                if (conv != null) {
                    val lastModified = maxOf(cachedConv.date, conv.date)
                    val conversation = conv.copy(date = lastModified)
                    insertOrUpdateConversation(conversation)
                }
            }

            val allConversations = conversationsDB.getNonArchived() as ArrayList<Conversation>
            runOnUiThread {
                setupConversations(allConversations)
            }

            if (config.appRunCount == 1) {
                conversations.map { it.threadId }.forEach { threadId ->
                    val messages = getMessages(threadId, getImageResolutions = false, includeScheduledMessages = false)
                    messages.chunked(30).forEach { currentMessages ->
                        messagesDB.insertMessages(*currentMessages.toTypedArray())
                    }
                }
            }
        }
    }

    private fun getOrCreateConversationsAdapter(): ConversationsAdapter {
        var currAdapter = binding.rec.adapter
        if (currAdapter == null) {
            hideKeyboard()
            currAdapter = ConversationsAdapter(
                activity = this,
                recyclerView = binding.rec,
                onRefresh = { notifyDatasetChanged() },
                itemClick = { handleConversationClick(it) }
            )

            binding.rec.adapter = currAdapter
            if (areSystemAnimationsEnabled) {
                binding.rec.scheduleLayoutAnimation()
            }
        }
        return currAdapter as ConversationsAdapter
    }

    private fun setupConversations(conversations: ArrayList<Conversation>, cached: Boolean = false) {
        val sortedConversations = conversations.sortedWith(
            compareByDescending<Conversation> { config.pinnedConversations.contains(it.threadId.toString()) }
                .thenByDescending { it.date }
        ).toMutableList() as ArrayList<Conversation>

        if (cached && config.appRunCount == 1) {
            showOrHideProgress(conversations.isEmpty())
        } else {
            showOrHideProgress(false)
            showOrHidePlaceholder(conversations.isEmpty())
        }

        try {
            getOrCreateConversationsAdapter().apply {
                updateConversations(sortedConversations) {
                    if (!cached) {
                        showOrHidePlaceholder(currentList.isEmpty())
                    }
                }
            }
        } catch (ignored: Exception) {
        }
    }

    private fun showOrHideProgress(show: Boolean) {
        if (show) {
            binding.conversationsProgressBar.show()
            binding.noConversationsPlaceholder.beVisible()
            binding.noConversationsPlaceholder.text = getString(R.string.loading_messages)
        } else {
            binding.conversationsProgressBar.hide()
            binding.noConversationsPlaceholder.beGone()
        }
    }

    private fun showOrHidePlaceholder(show: Boolean) {
        binding.conversationsFastscroller.beGoneIf(show)
        binding.noConversationsPlaceholder.beVisibleIf(show)
        binding.noConversationsPlaceholder.text = getString(R.string.no_conversations_found)
        binding.noConversationsPlaceholder2.beVisibleIf(show)
    }

    private fun fadeOutSearch() {
        binding.searchHolder.animate().alpha(0f).setDuration(SHORT_ANIMATION_DURATION).withEndAction {
            binding.searchHolder.beGone()
            searchTextChanged("", true)
        }.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDatasetChanged() {
        getOrCreateConversationsAdapter().notifyDataSetChanged()
    }

    private fun handleConversationClick(any: Any) {
        Intent(this, ThreadActivity::class.java).apply {
            val conversation = any as Conversation
            putExtra(THREAD_ID, conversation.threadId)
            putExtra(THREAD_TITLE, conversation.title)
            putExtra(WAS_PROTECTION_HANDLED, wasProtectionHandled)
            startActivity(this)
        }
    }

    private fun rateUs() {
        hideKeyboard()
        RateUsFragment().show(supportFragmentManager,"")
    }

    private fun launchNewConversation() {
        hideKeyboard()
        Intent(this, NewConversationActivity::class.java).apply {
            startActivity(this)
        }
    }

    @SuppressLint("NewApi")
    private fun checkShortcut() {
        val appIconColor = config.appIconColor
        if (isNougatMR1Plus() && config.lastHandledShortcutColor != appIconColor) {
            val newConversation = getCreateNewContactShortcut(appIconColor)

            val manager = getSystemService(ShortcutManager::class.java)
            try {
                manager.dynamicShortcuts = listOf(newConversation)
                config.lastHandledShortcutColor = appIconColor
            } catch (ignored: Exception) {
            }
        }
    }

    @SuppressLint("NewApi")
    private fun getCreateNewContactShortcut(appIconColor: Int): ShortcutInfo {
        val newEvent = getString(R.string.new_conversation)
        val drawable = resources.getDrawable(com.simplemobiletools.commons.R.drawable.shortcut_plus)
        (drawable as LayerDrawable).findDrawableByLayerId(com.simplemobiletools.commons.R.id.shortcut_plus_background).applyColorFilter(appIconColor)
        val bmp = drawable.convertToBitmap()

        val intent = Intent(this, NewConversationActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        return ShortcutInfo.Builder(this, "new_conversation")
            .setShortLabel(newEvent)
            .setLongLabel(newEvent)
            .setIcon(Icon.createWithBitmap(bmp))
            .setIntent(intent)
            .build()
    }

    private fun searchTextChanged(text: String, forceUpdate: Boolean = false) {
        if (!binding.mainMenu.isSearchOpen && !forceUpdate) {
            return
        }

        lastSearchedText = text
        binding.searchPlaceholder2.beGoneIf(text.length >= 2)
        if (text.length >= 2) {
            ensureBackgroundThread {
                val searchQuery = "%$text%"
                val messages = messagesDB.getMessagesWithText(searchQuery)
                val conversations = conversationsDB.getConversationsWithText(searchQuery)
                if (text == lastSearchedText) {
                    showSearchResults(messages, conversations, text)
                }
            }
        } else {
            binding.searchPlaceholder.beVisible()
            binding.searchResultsList.beGone()
        }
    }

    private fun showSearchResults(messages: List<Message>, conversations: List<Conversation>, searchedText: String) {
        val searchResults = ArrayList<SearchResult>()
        conversations.forEach { conversation ->
            val date = conversation.date.formatDateOrTime(this, true, true)
            val searchResult = SearchResult(-1, conversation.title, conversation.phoneNumber, date, conversation.threadId, conversation.photoUri)
            searchResults.add(searchResult)
        }

        messages.sortedByDescending { it.id }.forEach { message ->
            var recipient = message.senderName
            if (recipient.isEmpty() && message.participants.isNotEmpty()) {
                val participantNames = message.participants.map { it.name }
                recipient = TextUtils.join(", ", participantNames)
            }

            val date = message.date.formatDateOrTime(this, true, true)
            val searchResult = SearchResult(message.id, recipient, message.body, date, message.threadId, message.senderPhotoUri)
            searchResults.add(searchResult)
        }

        runOnUiThread {
            binding.searchResultsList.beVisibleIf(searchResults.isNotEmpty())
            binding.searchPlaceholder.beVisibleIf(searchResults.isEmpty())

            val currAdapter = binding.searchResultsList.adapter
            if (currAdapter == null) {
                SearchResultsAdapter(this, searchResults, binding.searchResultsList, searchedText) {
                    hideKeyboard()
                    Intent(this, ThreadActivity::class.java).apply {
                        putExtra(THREAD_ID, (it as SearchResult).threadId)
                        putExtra(THREAD_TITLE, it.title)
                        putExtra(SEARCHED_MESSAGE_ID, it.messageId)
                        startActivity(this)
                    }
                }.apply {
                    binding.searchResultsList.adapter = this
                }
            } else {
                (currAdapter as SearchResultsAdapter).updateItems(searchResults, searchedText)
            }
        }
    }

    private fun launchRecycleBin() {
        hideKeyboard()
        startActivity(Intent(applicationContext, RecycleBinConversationsActivity::class.java))
    }

    private fun launchArchivedConversations() {
        hideKeyboard()
        startActivity(Intent(applicationContext, ArchivedConversationsActivity::class.java))
    }

    private fun launchSettings() {
        hideKeyboard()
        startActivity(Intent(applicationContext, SettingsActivity::class.java))
    }

    private fun launchAbout() {
        val licenses = LICENSE_EVENT_BUS or LICENSE_SMS_MMS or LICENSE_INDICATOR_FAST_SCROLL

        val faqItems = arrayListOf(
            FAQItem(R.string.faq_2_title, R.string.faq_2_text),
            FAQItem(R.string.faq_3_title, R.string.faq_3_text),
            FAQItem(com.simplemobiletools.commons.R.string.faq_9_title_commons, com.simplemobiletools.commons.R.string.faq_9_text_commons)
        )

        if (!resources.getBoolean(com.simplemobiletools.commons.R.bool.hide_google_relations)) {
            faqItems.add(FAQItem(com.simplemobiletools.commons.R.string.faq_2_title_commons, com.simplemobiletools.commons.R.string.faq_2_text_commons))
            faqItems.add(FAQItem(com.simplemobiletools.commons.R.string.faq_6_title_commons, com.simplemobiletools.commons.R.string.faq_6_text_commons))
        }

        startAboutActivity(R.string.app_name, licenses, BuildConfig.VERSION_NAME, faqItems, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshMessages(event: Events.RefreshMessages) {
        checkPermissions()
    }

    private fun checkWhatsNewDialog() {
        arrayListOf<Release>().apply {
            add(Release(48, R.string.release_48))
            add(Release(62, R.string.release_62))
            checkWhatsNew(this, BuildConfig.VERSION_CODE)
        }
    }

    companion object {
        const val WAS_PROTECTION_HANDLED = "was_protection_handled"
    }
}
