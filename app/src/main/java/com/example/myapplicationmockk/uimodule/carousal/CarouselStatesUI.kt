package com.viewlift.uimodule.carousal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viewlift.common.label.BootstrapColors
import com.viewlift.common.label.CarouselColors
import com.viewlift.common.label.CarouselLabels
import com.viewlift.common.utils.parse

@Composable
fun GamePreview() {

    Box(modifier = Modifier
        .background(
            color = "#C4CED4".parse,
            shape = RoundedCornerShape(2.dp)
        )
        .padding(5.dp, 2.dp, 5.dp, 2.dp),
        contentAlignment = Alignment.Center,

    ){
        Text(
            text = CarouselLabels.gamePreview,
            color ="#152d4d".parse,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            style = TextStyle(letterSpacing = 1.sp)
        )
    }

}

@Composable
fun PreGamePreview() {
    Row(modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            text = CarouselLabels.preGame,
            color = "#152d4d".parse,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            style = TextStyle(letterSpacing = 1.sp),
            modifier = Modifier.background(
                color = "#C4CED4".parse,
                shape = RoundedCornerShape(2.dp, 0.dp, 0.dp, 2.dp)
            ).padding(5.dp, 1.dp, 5.dp, 1.dp)
        )

        Text(
            text = CarouselLabels.live,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            style = TextStyle(letterSpacing = 1.sp),
            modifier =  Modifier.background(
                color = CarouselColors.liveGameColor.parse,
                shape = RoundedCornerShape(0.dp, 2.dp, 2.dp, 0.dp)
            ).padding(5.dp, 1.dp, 5.dp, 1.dp)
        )
    }
}

@Composable
fun LiveGame() {
    Box(modifier = Modifier
        .background(
            color = CarouselColors.liveGameColor.parse,
            shape = RoundedCornerShape(2.dp)
        )
        .padding(5.dp, 1.dp, 5.dp, 1.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = CarouselLabels.live,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            style = TextStyle(letterSpacing = 1.sp)
        )
    }
}

@Composable
fun PostGame() {
    Row(modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(
            text = CarouselLabels.postGame,
            color = "#152d4d".parse,
            fontWeight = FontWeight.SemiBold,
            fontSize = 8.sp,
            style = TextStyle(letterSpacing = 1.sp),
            modifier = Modifier.background(
                color = "#C4CED4".parse,
                shape = RoundedCornerShape(2.dp, 0.dp, 0.dp, 2.dp)
            ).padding(5.dp, 1.dp, 5.dp, 1.dp)
        )

        Text(
            text = CarouselLabels.live,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 8.sp,
            style = TextStyle(letterSpacing = 1.sp),
            modifier =  Modifier.background(
                color = CarouselColors.liveGameColor.parse,
                shape = RoundedCornerShape(0.dp, 2.dp, 2.dp, 0.dp)
            ).padding(5.dp, 1.dp, 5.dp, 1.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column {
        GamePreview()

        Spacer(modifier = Modifier.height(10.dp))

        PreGamePreview()

        Spacer(modifier = Modifier.height(10.dp))


        LiveGame()

        Spacer(modifier = Modifier.height(10.dp))

        PostGame()

    }

}

