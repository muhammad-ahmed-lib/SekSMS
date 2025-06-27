package com.simplemobiletools.smsmessenger.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.presentation.ui.screens.main.MainActivity
import com.simplemobiletools.smsmessenger.presentation.ui.screens.splash.SplashScreenComposable
import com.simplemobiletools.smsmessenger.utils.TinyDB
import com.simplemobiletools.smsmessenger.utils.openActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SplashScreenComposable()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            if (TinyDB(requireContext()).getToken()){
                requireContext().openActivity<MainActivity>()
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_LaungaugeSelectionFragment)
            }
        }
    }
}


