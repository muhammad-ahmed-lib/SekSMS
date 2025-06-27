package com.simplemobiletools.smsmessenger.presentation.ui.screens.onBoarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simplemobiletools.smsmessenger.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.simplemobiletools.smsmessenger.presentation.ui.componenets.AppText
import kotlinx.coroutines.launch



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(gotoNextScreen:()->Unit) {


    val scope = rememberCoroutineScope()
    var pagerState = rememberPagerState(pageCount = {
        onBoardingItems.size
    }
    )
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { pages ->
            val item = onBoardingItems[pages]
            CustomOnBoarding(item)
        }
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row() {
                    repeat(3) { index ->
                        val color = if (pagerState.currentPage == index) {
                            Color(0xFF2387E0)
                        } else {
                            Color.LightGray
                        }
                        val width = if (pagerState.currentPage == index) 16.dp else 8.dp
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
                AppText(
                    if (pagerState.currentPage < onBoardingItems.size -1) "Next" else "Let’s Go",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    color = colorResource(R.color.primary_Color),
                    modifier = Modifier.clickable {
                        if (pagerState.currentPage < onBoardingItems.size -1) {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                        }else{
                            gotoNextScreen.invoke()
                        }
                    },
                    )

            }
        }
    }

}


@Composable
fun CustomOnBoarding(item: OnboardingItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = item.imageRes), contentDescription = "", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        AppText(
            item.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.W600,
            color = colorResource(R.color.charcoal_color)
        )

        Spacer(modifier = Modifier.height(16.dp))
        AppText(
            item.text,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = colorResource(R.color.light_grey),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
            textAlign = TextAlign.Center
        )
    }
}

val onBoardingItems = listOf(
    OnboardingItem(
        R.drawable.first,
        "Messages",
        "Messages app can connect with people everywhere, send SMS, text message without internet connection."
    ),
    OnboardingItem(
        R.drawable.second,
        "Easily Organize",
        "Your message inbox"
    ),
    OnboardingItem(
        R.drawable.third,
        "Secure",
        "Your all message and chat"
    ),
)

data class OnboardingItem(
    val imageRes: Int,
    val title: String,
    val text: String
)
