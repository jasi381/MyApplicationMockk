package com.viewlift.uimodule.schedule.ui.component.button


import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomCalenderIconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .wrapContentSize()
            .clip(CircleShape)
    ) {
        Icon(
            modifier = Modifier,
            tint = Color.White,
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}
