package com.simplemobiletools.smsmessenger.presentation.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.simplemobiletools.smsmessenger.R

@Composable
fun SplashScreenComposable() {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.splash_bg)),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(R.drawable.ic_splash_bg),
            contentDescription = null,
            modifier = Modifier.size(width = 200.dp, height = 200.dp),
            contentScale = ContentScale.Fit
        )


    }
}
