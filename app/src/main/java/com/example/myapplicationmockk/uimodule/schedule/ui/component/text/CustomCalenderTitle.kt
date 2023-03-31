package com.viewlift.uimodule.schedule.ui.component.text


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CustomCalenderTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.SemiBold,
    customCalenderTextConfig: CustomCalenderTextConfig = CustomCalenderTextDefaults.customCalenderTitleTextConfig()
) {
    Text(
        modifier = modifier,
        color = customCalenderTextConfig.customCalenderTextColor.textColor,
        fontSize = customCalenderTextConfig.customCalenderTextSize.size,
        text = text,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Preview
@Composable
private fun CustomCalenderTitlePreview() {
    CustomCalenderTitle(modifier = Modifier, text = "Hye Himanshu")
}
