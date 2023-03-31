package com.viewlift.uimodule.schedule.ui.component.text


data class CustomCalenderTextConfig(
    val customCalenderTextColor: CustomCalenderTextColor = CustomCalenderTextColorDefaults.customCalenderTitleTextColor(),
    val customCalenderTextSize: CustomCalenderTextSize = CustomCalenderTextSize.Title
)

internal object CustomCalenderTextDefaults {

    fun customCalenderTitleTextConfig() = CustomCalenderTextConfig(
        customCalenderTextColor = CustomCalenderTextColorDefaults.customCalenderTitleTextColor(),
        customCalenderTextSize = CustomCalenderTextSize.Title
    )

    fun customCalenderSubTitleTextConfig() = CustomCalenderTextConfig(
        customCalenderTextColor = CustomCalenderTextColorDefaults.customCalenderTitleTextColor(),
        customCalenderTextSize = CustomCalenderTextSize.SubTitle
    )

    fun customCalenderNormalTextConfig() = CustomCalenderTextConfig(
        customCalenderTextColor = CustomCalenderTextColorDefaults.customCalenderNormalTextColor(),
        customCalenderTextSize = CustomCalenderTextSize.Normal
    )
}
