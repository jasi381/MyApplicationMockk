package com.viewlift.uimodule.button

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewlift.uimodule.data.Module
import com.viewlift.uimodule.tray.util.TrayUtil


@Composable
fun VLSeeMoreButton(modifier: Modifier, module: Module) {

    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = contentColorFor(backgroundColor = TrayUtil.trayBgColor)
        ),
        modifier = modifier
            .border(
            2.dp,
            //Color(0x75000000 + borderColor.toArgb()),
            //Color(0x25000000 +TrayUtil.TryaBgColor(settings).toArgb()),
            TrayUtil.TryaButtonBorderColor(module.layout?.settings),
            //borderColor,
            RoundedCornerShape(4.dp)
        )
    ) {
        val seeMoreText = if(module.metadataMap?.get("seeMoreTxt") != null){
            module.metadataMap["seeMoreTxt"]
        }else{
            TrayUtil.seeMoreCTA
        }
        Text(text = seeMoreText!!, style = TrayUtil.TypoGraphyMap["trayTitle"]!!.copy(fontSize = 14.sp))
    }
}