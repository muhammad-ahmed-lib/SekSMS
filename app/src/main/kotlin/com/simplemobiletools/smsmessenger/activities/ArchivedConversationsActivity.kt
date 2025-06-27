package com.simplemobiletools.smsmessenger.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.simplemobiletools.commons.dialogs.ConfirmationDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.root.appstate.BasicActivity
import com.simplemobiletools.smsmessenger.adapters.ArchivedConversationsAdapter
import com.simplemobiletools.smsmessenger.databinding.ActivityArchivedConversationsBinding
import com.simplemobiletools.smsmessenger.databinding.DrawerRecItemBinding
import com.simplemobiletools.smsmessenger.extensions.*
import com.simplemobiletools.smsmessenger.helpers.*
import com.simplemobiletools.smsmessenger.models.Conversation
import com.simplemobiletools.smsmessenger.models.Events
import com.simplemobiletools.smsmessenger.presentation.ui.adapters.GenericAdapter
import com.simplemobiletools.smsmessenger.presentation.ui.screens.main.MainActivity
import com.simplemobiletools.smsmessenger.presentation.ui.screens.main.MainActivity.DrawerModel
import com.simplemobiletools.smsmessenger.utils.loadImage
import com.simplemobiletools.smsmessenger.utils.openActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ArchivedConversationsActivity : BasicActivity() {
    private var bus: EventBus? = null
    private val binding by viewBinding(ActivityArchivedConversationsBinding::inflate)
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOptionsMenu()



        updateMaterialActivityViews(
            mainCoordinatorLayout =null,
            nestedView = binding.conversationsList,
            useTransparentNavigation = true,
            useTopSearchMenu = false
        )
       /*
        setupMaterialScrollListener(scrollingView = binding.conversationsList, toolbar = binding.archiveToolbar)
*/
        loadArchivedConversations()
        drawer()
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
                        R.drawable.inbox_icon-> openActivity<MainActivity>()
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

                }
            }
        }
        binding.drawerInclude.rec.adapter=adapte

    }
    override fun onResume() {
        super.onResume()
      //  setupToolbar(binding.archiveToolbar, NavigationIcon.Arrow)
        updateMenuColors()

        loadArchivedConversations()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus?.unregister(this)
    }

    private fun setupOptionsMenu() {
       /* binding.archiveToolbar.inflateMenu(R.menu.archive_menu)
        binding.archiveToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.empty_archive -> removeAll()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }*/
    }

    private fun updateOptionsMenu(conversations: ArrayList<Conversation>) {
        /*binding.archiveToolbar.menu.apply {
            findItem(R.id.empty_archive).isVisible = conversations.isNotEmpty()
        }*/
    }

    private fun updateMenuColors() {
        updateStatusbarColor(getProperBackgroundColor())
    }

    private fun loadArchivedConversations() {
        ensureBackgroundThread {
            val conversations = try {
                conversationsDB.getAllArchived().toMutableList() as ArrayList<Conversation>
            } catch (e: Exception) {
                ArrayList()
            }

            runOnUiThread {
                setupConversations(conversations)
            }
        }

        bus = EventBus.getDefault()
        try {
            bus!!.register(this)
        } catch (ignored: Exception) {
        }
    }

    private fun removeAll() {
        ConfirmationDialog(
            activity = this,
            message = "",
            messageId = R.string.empty_archive_confirmation,
            positive = com.simplemobiletools.commons.R.string.yes,
            negative = com.simplemobiletools.commons.R.string.no
        ) {
            removeAllArchivedConversations {
                loadArchivedConversations()
            }
        }
    }

    private fun getOrCreateConversationsAdapter(): ArchivedConversationsAdapter {
        var currAdapter = binding.conversationsList.adapter
        if (currAdapter == null) {
            hideKeyboard()
            currAdapter = ArchivedConversationsAdapter(
                activity = this,
                recyclerView = binding.conversationsList,
                onRefresh = { notifyDatasetChanged() },
                itemClick = { handleConversationClick(it) }
            )

            binding.conversationsList.adapter = currAdapter
            if (areSystemAnimationsEnabled) {
                binding.conversationsList.scheduleLayoutAnimation()
            }
        }
        return currAdapter as ArchivedConversationsAdapter
    }

    private fun setupConversations(conversations: ArrayList<Conversation>) {
        val sortedConversations = conversations.sortedWith(
            compareByDescending<Conversation> { config.pinnedConversations.contains(it.threadId.toString()) }
                .thenByDescending { it.date }
        ).toMutableList() as ArrayList<Conversation>

        showOrHidePlaceholder(conversations.isEmpty())
        updateOptionsMenu(conversations)

        try {
            getOrCreateConversationsAdapter().apply {
                updateConversations(sortedConversations)
            }
        } catch (ignored: Exception) {
        }
    }

    private fun showOrHidePlaceholder(show: Boolean) {
        binding.conversationsFastscroller.beGoneIf(show)
        binding.emptyDataLayout.beVisibleIf(show)
        binding.noConversationsPlaceholder.setTextColor(getProperTextColor())
        binding.noConversationsPlaceholder.text = getString(R.string.no_archived_conversations)
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
            putExtra(WAS_PROTECTION_HANDLED, true)
            startActivity(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshMessages(event: Events.RefreshMessages) {
        loadArchivedConversations()
    }
}
