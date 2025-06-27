package com.simplemobiletools.smsmessenger.presentation.ui.screens.permission

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.presentation.ui.screens.onBoarding.OnBoardingScreen


class PermisssionFragment : Fragment() {
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var phonePermissionLauncher: ActivityResultLauncher<String>

    private var isNotificationPermissionGranted = false
    private var isPhonePermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            isNotificationPermissionGranted = isGranted
            Toast.makeText(requireContext(), "Notification Permission: $isGranted", Toast.LENGTH_SHORT).show()
            checkAllPermissionsAndNavigate()
        }

        phonePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            isPhonePermissionGranted = isGranted
            Toast.makeText(requireContext(), "Phone Permission: $isGranted", Toast.LENGTH_SHORT).show()
            checkAllPermissionsAndNavigate()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                 PermissionScreen(
                     onRequestPermissions = {
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                             notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                         } else {
                             isNotificationPermissionGranted = true
                         }
                         phonePermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                     }
                 )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun checkAllPermissionsAndNavigate() {
        if (isNotificationPermissionGranted && isPhonePermissionGranted) {
            // Navigate to next fragment
            findNavController().navigate(R.id.action_permissionFragment_to_OnboradingFragement)
        }
    }
}
