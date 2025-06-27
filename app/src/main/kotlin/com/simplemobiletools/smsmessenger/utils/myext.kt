package com.simplemobiletools.smsmessenger.utils
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.simplemobiletools.smsmessenger.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri


fun String.highlightWord(targetWord: String, color: Int): SpannableString {
    val spannable = SpannableString(this)
    val startIndex = this.indexOf(targetWord)

    if (startIndex != -1) {
        spannable.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            startIndex + targetWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannable
}

fun TextView.setHighlightedText(fullText: String, targetWord: String, color: Int) {
    this.text = fullText.highlightWord(targetWord, color)
}

fun Context.openWebLink(url: String="https://policies.google.com/privacy?hl=en-US", errorMessage: String = "Unable to open link") {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

fun Context.shareApp() {
    val textToShare="Check out this app: https://play.google.com/store/apps/details?id=${packageName}"
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, textToShare)
        type = "text/plain"
    }
    val chooser = Intent.createChooser(intent, "Share via")
    // Ensure there's an app to handle the intent
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(chooser)
    }
}

// Helper functions
fun Context.getAppName(): String {
    return try {
        val packageName = packageName
        val pm: PackageManager = packageManager
        val appInfo = pm.getApplicationInfo(packageName, 0)
        pm.getApplicationLabel(appInfo).toString()
    } catch (e: Exception) {
        "this app"
    }
}

fun Context.getPlayStoreLink(): String {
    return "https://play.google.com/store/apps/details?id=${packageName}"
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireActivity().showToast(message,duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
fun View.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}
inline fun <reified T : Activity> Context.openActivity() {
    startActivity(Intent(this, T::class.java))
}

fun Context.openActivity(activityClass: Class<out Activity>) {
    startActivity(Intent(this, activityClass))
}

inline fun <reified T : Activity> Context.openActivityWithExtras(vararg pairs: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    pairs.forEach { (key, value) ->
        when (value) {
            is String -> intent.putExtra(key, value)
            is Int -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            // add other types as needed
        }
    }
    startActivity(intent)
}
fun Activity.hideKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}
private fun ImageView.canLoadImage(): Boolean {
    val context = this.context
    val activity = when (context) {
        is Activity -> context
        is ContextWrapper -> context.baseContext as? Activity
        else -> null
    }
    return activity?.isDestroyed == false && activity?.isFinishing == false
}

// Load from URL
fun ImageView.loadImage(url: String?) {
    if (!canLoadImage()) return
    Glide.with(this.context)
        .load(url)
        .placeholder(android.R.color.darker_gray)
        .into(this)
}

fun ImageView.loadImage(url: File?) {
    if (!canLoadImage()) return
    Glide.with(this.context)
        .load(url)
        .placeholder(android.R.color.darker_gray)
        .into(this)
}

// Load from drawable resource
fun ImageView.loadImage(@DrawableRes resId: Int) {
    if (!canLoadImage()) return
    Glide.with(this.context)
        .load(resId)
        .into(this)
}

// Load from Drawable
fun ImageView.loadImage(drawable: Drawable?) {
    if (!canLoadImage()) return
    Glide.with(this.context)
        .load(drawable)
        .into(this)
}

// Load from Bitmap
fun ImageView.loadImage(bitmap: Bitmap?) {
    if (!canLoadImage()) return
    Glide.with(this.context)
        .load(bitmap)
        .into(this)
}




// Extension property for TextView (Date)
var TextView.currentFormattedDate: String
    get() = this.text.toString()
    set(value) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        this.text = dateFormat.format(Date())
    }

// Extension property for TextView (Time)
var TextView.currentFormattedTime: String
    get() = this.text.toString()
    set(value) {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        this.text = timeFormat.format(Date())
    }

fun String.toFormattedDate(inputFormat: String = "yyyy-MM-dd"): String {
    return try {
        val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
        val date = inputDateFormat.parse(this)
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        outputDateFormat.format(date)
    } catch (e: Exception) {
        "" // or handle the error as you prefer
    }
}

// Extension function to parse a String to Date and then format time as HH:mm:ss
fun String.toFormattedTime(inputFormat: String = "HH:mm:ss"): String {
    return try {
        val inputTimeFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
        val date = inputTimeFormat.parse(this)
        val outputTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        outputTimeFormat.format(date)
    } catch (e: Exception) {
        "" // or handle the error as you prefer
    }
}
