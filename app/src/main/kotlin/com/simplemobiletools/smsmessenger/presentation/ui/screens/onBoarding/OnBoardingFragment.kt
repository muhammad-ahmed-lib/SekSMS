package com.simplemobiletools.smsmessenger.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.simplemobiletools.smsmessenger.presentation.ui.screens.main.MainActivity
import com.simplemobiletools.smsmessenger.presentation.ui.screens.onBoarding.OnBoardingScreen


class OnBoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                OnBoardingScreen(gotoNextScreen = {
                    moveTonext()
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun moveTonext(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }



}
