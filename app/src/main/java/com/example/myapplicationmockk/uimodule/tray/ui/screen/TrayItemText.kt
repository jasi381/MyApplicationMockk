package com.viewlift.uimodule.tray.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.viewlift.uimodule.R

import com.viewlift.uimodule.tray.util.TrayUtil
import com.viewlift.uimodule.data.ContentData
import com.viewlift.uimodule.data.Layout
import com.viewlift.uimodule.data.Settings
import com.viewlift.uimodule.tray.listener.TrayActionListener

@Composable
fun TrayItemText(
    data: ContentData,
    trayType: String,
    settings: Settings?
) {
    Column() {
        Text(
            text = data.gist?.title!!,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 4.dp, top = 4.dp, end = 4.dp, bottom = 0.dp)
                .width(TrayUtil.trayTextWidth(trayType)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (!TrayUtil.isRemoveDescription(settings) && data.gist?.description != null) {
            Text(
                text = data.gist?.description!!,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 4.dp, top = 0.dp, end = 4.dp, bottom = 16.dp)
                    .width(TrayUtil.trayTextWidth(trayType)),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}

@Composable
fun VLTray01ItemText(
    data: ContentData,
    textWidth: Dp,
    layout: Layout?,
    listener: TrayActionListener
) {
    ConstraintLayout(
        modifier = Modifier
            .width(textWidth)
            .padding(12.dp)
    ) {
        val (titleText, shareAction) = createRefs()
        val endBarrier = createStartBarrier(shareAction)

        Text(
            text = data.gist?.title!!,
            style = TrayUtil.textStyle(layout,"trayItemTitle"),
            textAlign = TextAlign.Start,
            modifier = Modifier
                // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .defaultMinSize(minHeight = 38.dp)
                .constrainAs(titleText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    //end.linkTo(parent.end)
                    end.linkTo(endBarrier, 0.dp)
                    width = Dimension.fillToConstraints
                },
            maxLines = 2,

            overflow = TextOverflow.Ellipsis
        )
        //if (!TrayUtil.isRemoveDescription(settings) && data.gist?.description != null) {
        if (layout?.settings?.enableSharing == true) {


            Image(
                painter = painterResource(id = R.drawable.icon_share),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(shareAction) {
                        top.linkTo(titleText.top)
                        end.linkTo(parent.end)
                        start.linkTo(titleText.end, 20.dp)
                    }
                    .clickable { listener.shareFromTrayAction(data.gist?.permalink!!) }
            ) /*{

                Image(painter = painterResource(id = R.drawable.icon_share), contentDescription = null )
            }*/
        }
    }

}


@Composable
fun VLTrayVerticalItemText(
    data: ContentData,
    settings: Settings?,
    listener: TrayActionListener,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .padding(12.dp)
    ) {
        val (titleText, shareAction) = createRefs()
        val endBarrier = createStartBarrier(shareAction)

        Text(
            text = data.gist?.title!!,
            style = TrayUtil.TypoGraphyMap["trayItemTitle"]!!,
            textAlign = TextAlign.Start,
            modifier = Modifier
                // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .constrainAs(titleText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    //end.linkTo(parent.end)
                    end.linkTo(endBarrier, 0.dp)
                    width = Dimension.fillToConstraints
                },
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        //if (!TrayUtil.isRemoveDescription(settings) && data.gist?.description != null) {
        if (settings?.enableSharing == true) {


            Image(
                painter = painterResource(id = R.drawable.icon_share),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(shareAction) {
                        top.linkTo(titleText.top)
                        end.linkTo(parent.end)
                        start.linkTo(titleText.end, 20.dp)
                    }
                    .clickable { listener.shareFromTrayAction(data.gist.permalink!!) }
            ) /*{

                Image(painter = painterResource(id = R.drawable.icon_share), contentDescription = null )
            }*/
        }
    }

}

@Composable
fun VLTrayPlayListItemText(
    data: ContentData,
    trayType: String,
    settings: Settings?
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(12.dp)
            .width(TrayUtil.trayTextWidth(trayType))

    ) {
        val (infoText, infoIcon) = createRefs()
        //val endBarrier = createStartBarrier(shareAction)
        val runtimeText = TrayUtil.getRuntime(data.runtime)


        Image(
            painter = painterResource(id = R.drawable.ic_audio),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(infoIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(16.dp))
        Text(
            text = "5 MIN", // Todo dynamic
            style = TrayUtil.TypoGraphyMap["trayItemTitle"]!!.copy(fontSize = 10.sp),
            textAlign = TextAlign.Start,
            modifier = Modifier
                // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .constrainAs(infoText) {
                    start.linkTo(infoIcon.end, 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }

}