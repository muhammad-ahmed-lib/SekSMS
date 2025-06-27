package com.simplemobiletools.smsmessenger.presentation.ui.screens.permission

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.presentation.ui.componenets.*

@Composable
fun PermissionScreen(
    onRequestPermissions: () -> Unit

) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .systemBarsPadding()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(

            painter = painterResource(R.drawable.img_permission),
            contentDescription = "Permission",
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .customBorder(
                    color = colorResource(id = R.color.border_color),
                    strokeWidth = 2.dp.value,
                    showBottom = true,
                )
                .padding(vertical = 20.dp, horizontal = 80.dp),
            contentScale = ContentScale.Fit
        )
        PermissionItem(
            title = "Show notification",
            subTitle = "Allows an app to post notifications."
        )
        Spacer(modifier = Modifier.height(15.dp))
        PermissionItem(
            icon = R.drawable.ic_phonecall,
            title = "Phone permission",
            subTitle = "We access your call status during both incoming & outgoing calls to monitor the call’s activity & display after call screen information. This feature improves call management by providing you with relevant call details after call ends."
        )
        Spacer(modifier = Modifier.weight(1f))

        AnnotatedTextComponent(
            modifier = Modifier.padding(horizontal = 37.dp),
            parts = listOf(
                AnnotatedTextPart(
                    text = "By tapping agree & continue, you indicate that you have read our ",
                    color = colorResource(R.color.light_grey),
                    fontSize = 12.sp
                ),
                AnnotatedTextPart(
                    text = "Privacy policy Terms & conditions",
                    tag = "terms",
                    color = colorResource(R.color.primary_Color),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 12.sp
                )
            ),
            onTagClick = { tag ->
                if (tag == "terms") {
                    Toast.makeText(context, "Terms & Privacy Clicked", Toast.LENGTH_SHORT).show()
                }
            }
        )

        Spacer(Modifier.height(15.dp))

        CustomButton(
            text = "Agree & Continue",
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = colorResource(R.color.primary_Color),
            textColor = Color.White,
            cornerRadius = 8.dp,
            fontWeight = FontWeight.W400,
            onClick = {
                onRequestPermissions.invoke()

            }
        )


    }


}


@Composable
fun PermissionItem(
    icon: Int = R.drawable.ic_bell,
    title: String = "",
    subTitle: String = "",

    ) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .customBorder(
                color = colorResource(id = R.color.border_color),
                strokeWidth = 2.dp.value,
                showBottom = true,
            )
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {


        Image(
            painter = painterResource(icon),
            contentDescription = "Permission",
            modifier = Modifier
        )
        Spacer(Modifier.width(20.dp))
        Column(
            Modifier
                .wrapContentHeight()
                .customBorder(
                    color = colorResource(id = R.color.border_color),
                    strokeWidth = 2.dp.value,
                    showStart = true,
                )
                .padding(start = 15.dp),

            ) {

            AppText(
                title,
                color = colorResource(R.color.charcoal_color),
                fontWeight = FontWeight.W500,
                fontSize = 14.sp

            )
            AppText(

                subTitle,
                color = colorResource(R.color.light_grey),
                fontWeight = FontWeight.W400,
                fontSize = 12.sp

            )


        }


    }


}



