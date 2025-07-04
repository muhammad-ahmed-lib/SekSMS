package com.simplemobiletools.smsmessenger.adapters

import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter
import com.simplemobiletools.commons.databinding.ItemContactWithNumberBinding
import com.simplemobiletools.commons.extensions.getTextSize
import com.simplemobiletools.commons.helpers.SimpleContactsHelper
import com.simplemobiletools.commons.models.SimpleContact
import com.simplemobiletools.commons.views.MyRecyclerView
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.activities.SimpleActivity
import com.simplemobiletools.smsmessenger.activities.root.appstate.BaseSimpleCopy
import com.simplemobiletools.smsmessenger.activities.root.appstate.MyRecyclerViewAdapterCopyBaseSImpleCopy
import com.simplemobiletools.smsmessenger.databinding.ContactsRecItemBinding

class ContactsAdapter(
    activity: BaseSimpleCopy,
    var contacts: ArrayList<SimpleContact>,
    recyclerView: MyRecyclerView,
    itemClick: (Any) -> Unit
) : MyRecyclerViewAdapterCopyBaseSImpleCopy(activity, recyclerView, itemClick) {
    private var fontSize = activity.getTextSize()

    override fun getActionMenuId() = 0

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = contacts.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = contacts.getOrNull(position)?.rawId

    override fun getItemKeyPosition(key: Int) = contacts.indexOfFirst { it.rawId == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContactsRecItemBinding.inflate(layoutInflater, parent, false)
        return createViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bindView(contact, allowSingleClick = true, allowLongClick = false) { itemView, _ ->
            setupView(itemView, contact)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = contacts.size

    fun updateContacts(newContacts: ArrayList<SimpleContact>) {
        val oldHashCode = contacts.hashCode()
        val newHashCode = newContacts.hashCode()
        if (newHashCode != oldHashCode) {
            contacts = newContacts
            notifyDataSetChanged()
        }
    }

    private fun setupView(view: View, contact: SimpleContact) {
        ContactsRecItemBinding.bind(view).apply {
            nameTv.apply {
                text = contact.name
             //   setTextColor(view.context.getColor(R.color.textColor333333))
             //   setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize * 1.1f)
            }

            phoneTv.apply {
                text = TextUtils.join(", ", contact.phoneNumbers.map { it.normalizedNumber })
              //  setTextColor(view.context.getColor(R.color.textGrayaaaaaa))
               // setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
            }

            SimpleContactsHelper(activity).loadContactImage(contact.photoUri, contactImg, contact.name)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (!activity.isDestroyed && !activity.isFinishing) {
            val binding = ContactsRecItemBinding.bind(holder.itemView)
            Glide.with(activity).clear(binding.contactImg)
        }
    }
}
