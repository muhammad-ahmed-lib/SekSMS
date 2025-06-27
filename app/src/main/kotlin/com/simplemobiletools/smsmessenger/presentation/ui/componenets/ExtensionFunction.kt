package com.simplemobiletools.smsmessenger.presentation.ui.componenets

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


fun Modifier.customBorder(
    color: Color,
    strokeWidth: Float,
    showTop: Boolean = false,
    showBottom: Boolean = false,
    showStart: Boolean = false,
    showEnd: Boolean = false
) = this.drawWithContent {
    drawContent()

    if (showTop) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = strokeWidth
        )
    }

    if (showBottom) {
        drawLine(
            color = color,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = strokeWidth
        )
    }

    if (showStart) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = strokeWidth
        )
    }

    if (showEnd) {
        drawLine(
            color = color,
            start = Offset(size.width, 0f),
            end = Offset(size.width, size.height),
            strokeWidth = strokeWidth
        )
    }
}
