package com.example.myapplication.presentation.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.databinding.FragmentRateUsBinding


class RateUsFragment :  DialogFragment() {
    private var _binding: FragmentRateUsBinding? = null
    private val binding get() = _binding!!
    private var selectedRating = 5 // Default full rating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRateUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRatingStars()
        handleActions()
    }

    private fun setupRatingStars() {
        val starViews = listOf(
            binding.startOne,
            binding.startTwo,
            binding.startThree,
            binding.startFour,
            binding.startFive
        )

        starViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedRating = index + 1
                updateStars(starViews, selectedRating)
            }
        }
    }

    private fun updateStars(starViews: List<ImageView>, rating: Int) {
        starViews.forEachIndexed { index, imageView ->
            imageView.setImageResource(
                if (index < rating) R.drawable.selected_star
                else R.drawable.unselected_star // Add this icon if needed
            )
        }
    }

    private fun handleActions() {
        binding.submitBtn.setOnClickListener {
            if (selectedRating <= 3) {
                openEmailIntent()
            } else {
                openPlayStore()
            }
        }

        binding.cancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun openEmailIntent() {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("your_email@example.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Messages RateUs - $selectedRating Star Rating")
            putExtra(Intent.EXTRA_TEXT, "Hi Team,\n\nI rated the app $selectedRating stars. Here's my feedback:\n\n")
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Feedback"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No email app found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openPlayStore() {
        val appPackageName = requireContext().packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$appPackageName".toUri()
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$appPackageName".toUri()
                )
            )
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
