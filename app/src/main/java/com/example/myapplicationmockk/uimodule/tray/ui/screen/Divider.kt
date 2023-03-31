package com.viewlift.uimodule.tray.ui.screen

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TrayDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}

private const val DividerAlpha = 0.12f

/*
@Preview("default", showBackground = true)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DividerPreview() {
    //JetsnackTheme {
        Box(Modifier.size(height = 10.dp, width = 100.dp)) {
            TrayDivider(Modifier.align(Alignment.Center))
        }
    //}
}*/
