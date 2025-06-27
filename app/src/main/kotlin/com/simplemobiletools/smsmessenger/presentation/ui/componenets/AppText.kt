package com.simplemobiletools.smsmessenger.presentation.ui.componenets

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.simplemobiletools.smsmessenger.R

@Composable
fun AppText(
    text: String = "",
    fontWeight: FontWeight? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontFamily: FontFamily? = FontFamily(Font(R.font.outfit_regular)),
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Visible,
    textAlign: TextAlign? = null,
    modifier: Modifier=Modifier,

    ) {

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        modifier = modifier

        )

}



data class AnnotatedTextPart(
    val text: String,
    val tag: String? = null,
    val color: Color = Color.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontFamily: FontFamily? = FontFamily(Font(R.font.outfit_regular)),
    val textDecoration: TextDecoration? = null
)


@Composable
fun AnnotatedTextComponent(
    parts: List<AnnotatedTextPart>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    onTagClick: ((String) -> Unit)? = null
) {
    val annotatedString = buildAnnotatedString {
        parts.forEach { part ->
            if (part.tag != null) {
                pushStringAnnotation(tag = part.tag, annotation = part.text)
            }

            withStyle(
                style = SpanStyle(
                    color = part.color,
                    fontWeight = part.fontWeight,
                    fontSize = part.fontSize,
                    fontFamily = part.fontFamily,
                    textDecoration = part.textDecoration
                )
            ) {
                append(part.text)
            }

            if (part.tag != null) {
                pop()
            }
        }
    }

    if (onTagClick != null) {
        ClickableText(
            text = annotatedString,
            modifier = modifier,
            style = LocalTextStyle.current.copy(textAlign = textAlign),
            onClick = { offset ->
                annotatedString.getStringAnnotations(start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        onTagClick(annotation.tag)
                    }
            }
        )
    } else {
        Text(
            text = annotatedString,
            modifier = modifier,
            textAlign = textAlign
        )
    }
}
