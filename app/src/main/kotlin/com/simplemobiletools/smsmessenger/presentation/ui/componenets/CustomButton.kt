package com.simplemobiletools.smsmessenger.presentation.ui.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.simplemobiletools.smsmessenger.R


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.White,
    backgroundColor: Color = colorResource(R.color.primary_Color),
    cornerRadius: Dp = 12.dp,
    fontSize: TextUnit = TextUnit. Unspecified,
    padding: PaddingValues = PaddingValues(vertical = 15.dp, horizontal = 0.dp),
    border: BorderStroke? = null,
    fontWeight: FontWeight? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(padding)
            .then(if (border != null) Modifier.border(border, RoundedCornerShape(cornerRadius)) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontFamily = FontFamily(Font(R.font.outfit_regular)),
            fontWeight = fontWeight,
            fontSize = fontSize
        )
    }
}
