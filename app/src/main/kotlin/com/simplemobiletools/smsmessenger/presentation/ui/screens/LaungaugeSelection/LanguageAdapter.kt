package com.simplemobiletools.smsmessenger.presentation.ui.screens.LaungaugeSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simplemobiletools.smsmessenger.R

class LanguageAdapter(
    private val items: List<LanguageItem>,
    private val onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flag: ImageView = itemView.findViewById(R.id.ivFlag)
        val name: TextView = itemView.findViewById(R.id.tvLanguageName)
        val native: TextView = itemView.findViewById(R.id.tvNativeScript)
        val checkmark: ImageView = itemView.findViewById(R.id.ivCheckmark)

        init {
            itemView.setOnClickListener {
                onItemSelected(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val item = items[position]
        holder.flag.setImageResource(item.flagResId)
        holder.name.text = item.languageName
        holder.native.text = item.nativeScript
        holder.checkmark.visibility = if (item.isSelected) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = items.size
}


data class LanguageItem(
    val flagResId: Int,
    val languageName: String,
    val nativeScript: String,
    var isSelected: Boolean = false
)
