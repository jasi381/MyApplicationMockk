package com.viewlift.uimodule.schedule.ui.component.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomCalenderNormalText(
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight,
    textColor: Color,
    textSize: TextUnit = CustomCalenderTextSize.Normal.size

) {
    Text(
        modifier = modifier,
        color = textColor,
        fontSize = textSize,
        text = text,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun CustomCalenderNormalTextPreview() {
    CustomCalenderNormalText(
        text = "Hye Himanshu",
        modifier = Modifier,
        fontWeight = FontWeight.SemiBold,
        textColor = Color.Black,
        textSize = 26.sp
    )
}
