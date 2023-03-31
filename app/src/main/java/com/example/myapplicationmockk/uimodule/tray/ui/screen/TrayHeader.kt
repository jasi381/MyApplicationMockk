package com.viewlift.uimodule.tray.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.viewlift.uimodule.data.Layout
import com.viewlift.uimodule.data.Settings
import com.viewlift.uimodule.tray.listener.TrayActionListener
import com.viewlift.uimodule.tray.util.TrayUtil

@Composable
fun TrayHeader(
    title: String,
    settings: Settings?,
    trayTitleColor: Color,
    isDivider: Boolean,
    listener: TrayActionListener
) {
    if (TrayUtil.isComplexHeader(settings = settings)) {
        TrayComplexHeader(
            title = title,
            settings = settings,
            trayTitleColor = trayTitleColor,
            isDivider = isDivider,
            listener = listener
        )
    } else {
        TraySimpleHeader(
            title = title,
            trayTitleColor = trayTitleColor,
            isDivider = isDivider
        )
    }
}

@Composable
fun TrayComplexHeader(
    title: String,
    settings: Settings?,
    trayTitleColor: Color,
    isDivider: Boolean,
    listener: TrayActionListener,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            //.clickable { onSnackClick(snack.id) }
            .padding(horizontal = 0.dp)
    ) {
        val (trayHeaderIcon, divider, trayTitle, seeMoreAction) = createRefs()
        val startBarrier = createStartBarrier(seeMoreAction)
        val endBarrier = createEndBarrier(trayHeaderIcon)
        var placeholder: MemoryCache.Key? = null
        val imageUrl = settings?.trayIconUrl

        if (imageUrl != null) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                // .parameters(image.parameters)
                .build(),
                placeholder = ColorPainter(Color.Transparent),
                error = ColorPainter(Color.Red),
                onSuccess = { placeholder = it.result.memoryCacheKey },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp, 32.dp)
                    .constrainAs(trayHeaderIcon) {
                        start.linkTo(parent.start, 8.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = trayTitleColor,
            modifier = Modifier
                .constrainAs(trayTitle) {
                    start.linkTo(endBarrier, 8.dp)
                    top.linkTo(parent.top)
                    end.linkTo(startBarrier, 0.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(
            onClick = { listener.seeMoreTrayAction(settings?.seeAllPermalink!!) },
            modifier = Modifier
                .constrainAs(seeMoreAction) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                tint = trayTitleColor,
                contentDescription = settings?.showMoreCTA
            )
        }
        if (isDivider) {
            TrayDivider(Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            })
        }
    }
}

@Composable
fun TraySimpleHeader(
    title: String,
    trayTitleColor: Color,
    isDivider: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
    ) {
        val (divider, trayTitle) = createRefs()
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = trayTitleColor,
            modifier = Modifier
                .constrainAs(trayTitle) {
                    linkTo(
                        start = parent.start,
                        startMargin = 8.dp,
                        end = parent.end,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (isDivider) {
            TrayDivider(Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            })
        }
    }
}

@Composable
fun VLTray01Header(
    title: String,
    subtitle: String?,
    layout: Layout?,
    trayTitleColor: Color,
    isDivider: Boolean,
    listener: TrayActionListener,
    modifier: Modifier = Modifier
) {
    val localTitle = title.replace("\\n","\n")
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            //.clickable { onSnackClick(snack.id) }
            .padding(horizontal = 0.dp)
    ) {
        val (trayHeaderIcon, divider, textBox, trayTitle, traySubtitle) = createRefs()
        val subtitleBarrier = createBottomBarrier(traySubtitle)
        val endBarrier = createEndBarrier(trayHeaderIcon)
        var placeholder: MemoryCache.Key? = null
        val imageUrl = layout?.settings?.trayIconUrl
        var textPadding = 16.dp

        if (imageUrl != null && imageUrl.isNotEmpty()) {
            textPadding = 12.dp
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                // .parameters(image.parameters)
                .build(),
                placeholder = ColorPainter(Color.Transparent),
                error = ColorPainter(Color.Red),
                onSuccess = { placeholder = it.result.memoryCacheKey },
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .sizeIn(32.dp,32.dp,40.dp,40.dp)
                    .constrainAs(trayHeaderIcon) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(textBox.top)
                        bottom.linkTo(textBox.bottom)
                    })
        }

        Column(modifier = Modifier.constrainAs(textBox) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(endBarrier, textPadding)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }) {
            if(!subtitle.isNullOrEmpty()){
                Text(
                    text = subtitle,
                    style = TrayUtil.textStyle(layout,"traySubTitle"),
                    color = trayTitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = localTitle,
                style = TrayUtil.textStyle(layout,"trayTitle"),
                color = trayTitleColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (isDivider) {
            TrayDivider(Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            })
        }
    }
}

