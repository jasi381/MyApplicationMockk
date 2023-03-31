package com.viewlift.uimodule.tray.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.viewlift.uimodule.R
import com.viewlift.uimodule.data.ContentData
import com.viewlift.uimodule.data.Layout
import com.viewlift.uimodule.data.Settings
import com.viewlift.uimodule.tray.listener.TrayActionListener
import com.viewlift.uimodule.tray.util.TrayUtil
import com.viewlift.uimodule.utils.UiUtils


@Composable
fun TrayViewItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener
) {
    val trayType = layout?.blockName!!
    val settings = layout?.settings
    val isRoundCorner = TrayUtil.isRoundedCorner(settings)
    val shape =
        if (isRoundCorner) RoundedCornerShape(corner = CornerSize(16.dp)) else RoundedCornerShape(
            corner = CornerSize(0.dp)
        )
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .width(TrayUtil.trayTextWidth(trayType)),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = shape,
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                //.padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable { listener.clickItemTrayAction(data.gist?.id + " -- " + data.gist?.title) }
        ) {
            TrayImage(
                data = data,
                trayType = trayType,
                isRoundCorner = isRoundCorner,
                settings = settings,
                listener = listener
            )
            TrayItemText(
                data = data,
                trayType = trayType,
                settings = settings
            )
        }
    }
}

@Composable
fun VLTray01ViewItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener
) {

    val settings = layout?.settings
    val trayType = settings?.thumbnailType!!


    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .width(TrayUtil.trayTextWidth(trayType)),
            //.background(TrayUtil.TryaBgColor(settings), shape),
        elevation = CardDefaults.cardElevation(2.dp),
        shape =  RoundedCornerShape(corner = CornerSize(4.dp)),
        colors = CardDefaults.cardColors(TrayUtil.TryaBgColor(settings))
        // colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                //.padding(horizontal = 8.dp, vertical = 8.dp)
                .clickable { listener.clickItemTrayAction(data.gist?.id + " -- " + data.gist?.title) }
        ) {
            VLTray01Image(
                data = data,
                settings = settings,
                listener = listener
            )
            VLTray01ItemText(
                data = data,
                textWidth = TrayUtil.trayTextWidth(trayType),
                layout = layout,
                listener
            )

            if (data.gist?.contentType.equals("Playlist", true)|| data.gist?.contentType?.contains("Playlist", true) ?:false ) {
                VLTrayPlayListItemText(
                    data = data,
                    trayType = trayType,
                    settings = settings
                )
            }
        }
    }
}

@Composable
fun VLVerticalGrid(dataList: List<ContentData>,
                   layout: Layout?,
                   listener: TrayActionListener){

    LazyVerticalGrid(columns = GridCells.Fixed(1), content = {
        items(dataList.size){ index ->
            HighlightTray01ViewItem(
                data = dataList[index],
                layout = layout,
                listener = listener
            )
        }
    },
        modifier = Modifier
            .fillMaxWidth()
            .height(TrayUtil.gridHeight(layout?.settings, dataList.size))
        //.height(500.dp)
    )

}
@Composable
fun HighlightTray01ViewItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener
) {
    val settings = layout?.settings
    when (settings?.thumbnailPlacement) {
        "top" -> {
            HighlightTopImageTrayItem(data, layout, listener, false)
        }
        "left" -> {
            HighlightLeftImageTrayItem(data, layout, listener, false)
        }
        "right" -> {
            HighlightRightImageTrayItem(data, layout, listener, false)
        }
        else -> {
            HighlightLeftImageTrayItem(data, layout, listener, false)
        }
    }
}

@Composable
fun HighlightLeftImageTrayItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener,
    isShowDescription: Boolean
) {
    var placeholder: MemoryCache.Key? = null
    var thumbType = layout?.settings?.thumbnailType ?: "landscape"
    // thumbType = "32*9"
    val imageSize = TrayUtil.trayVerticalImageSize(thumbType)
    val imageUrl = TrayUtil.trayImage(thumbType, data.gist?.imageGist!!, imageSize)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            //.background(TrayUtil.TryaBgColor(settings),shape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        //colors = CardDefaults.cardColors(Color.Transparent)
        colors = CardDefaults.cardColors(TrayUtil.TryaBgColor(layout?.settings))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            val (thumbImage, iconType, titleText, itemInfo, shareAction) = createRefs()
            val endBarrier = createStartBarrier(shareAction)
            val topBarrier = createTopBarrier(iconType, itemInfo)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                placeholder = painterResource(id = TrayUtil.placeholderMap.get(thumbType)!!),
                error = painterResource(id = TrayUtil.placeholderMap.get(thumbType)!!),
                onSuccess = { placeholder = it.result.memoryCacheKey },
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .constrainAs(thumbImage) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        //height = Dimension.fillToConstraints
                        //end.linkTo(parent.end)

                    }
                    //.aspectRatio(16f/9f)
                    //.width(imageSize.width)
                    .size(imageSize)
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
            )
            Text(
                text = data.gist?.title!!,
                style = TrayUtil.textStyle(layout, "trayItemTitle"),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                    .constrainAs(titleText) {
                        start.linkTo(thumbImage.end, 12.dp)
                        top.linkTo(thumbImage.top, 12.dp)
                        bottom.linkTo(topBarrier)
                        //end.linkTo(parent.end)
                        end.linkTo(endBarrier, 12.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            /*Image(
                painter = painterResource(id = R.drawable.ic_article),
                contentDescription = null,
                modifier = Modifier.constrainAs(iconType) {
                    start.linkTo(titleText.start)
                    bottom.linkTo(parent.bottom, 12.dp)
                }
            )*/
            val publishedDate = if(data.publishDate!=null){
                UiUtils.getPublishDate(data.publishDate)
            } else {
                data.readTime
            }

            val info = TrayUtil.getAuthorName(publishedDate, data.author)
            Text(
                text = info,
                style = TrayUtil.textStyle(layout,"trayItemTitle").copy(
                    fontSize = 12.sp,
                    color = Color(0xff838996)
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                    .constrainAs(itemInfo) {
                        //start.linkTo(iconType.end, 8.dp)
                        //bottom.linkTo(iconType.bottom)


                        end.linkTo(parent.end, 12.dp)
                        start.linkTo(titleText.start)
                        bottom.linkTo(parent.bottom, 12.dp)

                        width = Dimension.fillToConstraints
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (layout?.settings?.enableSharing == true) {

                Image(
                    painter = painterResource(id = R.drawable.icon_share),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(shareAction) {
                            top.linkTo(titleText.top)
                            end.linkTo(parent.end,12.dp)
                            start.linkTo(titleText.end, 20.dp)
                        }
                        .clickable { listener.shareFromTrayAction(data.gist.permalink!!) }
                )
            }


        }
    }

}

@Composable
fun HighlightRightImageTrayItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener,
    isShowDescription: Boolean
) {
    var placeholder: MemoryCache.Key? = null
    var thumbType = layout?.settings?.thumbnailType ?: "landscape"
    // thumbType = "32*9"
    val imageSize = TrayUtil.trayVerticalImageSize(thumbType)
    val imageUrl = TrayUtil.trayImage(thumbType, data.gist?.imageGist!!, imageSize)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            //.background(TrayUtil.TryaBgColor(settings),shape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        //colors = CardDefaults.cardColors(Color.Transparent)
        colors = CardDefaults.cardColors(TrayUtil.TryaBgColor(layout?.settings))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            val (thumbImage, iconType, titleText, itemInfo, shareAction) = createRefs()
            val endBarrier = createStartBarrier(shareAction,thumbImage)
            val topBarrier = createTopBarrier(iconType, itemInfo)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                placeholder = painterResource(id = TrayUtil.placeholderMap.get(thumbType)!!),
                error = painterResource(id = TrayUtil.placeholderMap.get(thumbType)!!),
                onSuccess = { placeholder = it.result.memoryCacheKey },
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .constrainAs(thumbImage) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        //height = Dimension.fillToConstraints
                        //end.linkTo(parent.end)

                    }
                    //.aspectRatio(16f/9f)
                    //.width(imageSize.width)
                    .size(imageSize)
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
            )
            Text(
                text = data.gist?.title!!,
                style = TrayUtil.textStyle(layout, "trayItemTitle"),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                    .constrainAs(titleText) {
                        start.linkTo(parent.start, 12.dp)
                        top.linkTo(thumbImage.top, 12.dp)
                        bottom.linkTo(topBarrier)
                        //end.linkTo(parent.end)
                        end.linkTo(endBarrier, 12.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            /*Image(
                painter = painterResource(id = R.drawable.ic_article),
                contentDescription = null,
                modifier = Modifier.constrainAs(iconType) {
                    start.linkTo(titleText.start)
                    bottom.linkTo(parent.bottom, 12.dp)
                }
            )*/
            val publishedDate = if(data.publishDate!=null){
                "${UiUtils.getPublishDate(data.publishDate)}h"
            } else {
                data.readTime
            }

            val info = TrayUtil.getAuthorName(publishedDate, data.author)
            Text(
                text = info,
                style = TrayUtil.textStyle(layout,"trayItemTitle").copy(
                    fontSize = 12.sp,
                    color = Color(0xff838996)
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                    .constrainAs(itemInfo) {
                        //start.linkTo(iconType.end, 8.dp)
                        //bottom.linkTo(iconType.bottom)


                        end.linkTo(thumbImage.start, 12.dp)
                        start.linkTo(titleText.start)
                        bottom.linkTo(parent.bottom, 12.dp)

                        width = Dimension.fillToConstraints
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (layout?.settings?.enableSharing == true) {

                Image(
                    painter = painterResource(id = R.drawable.icon_share),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(shareAction) {
                            top.linkTo(titleText.top)
                            end.linkTo(thumbImage.start,12.dp)
                            start.linkTo(titleText.end, 20.dp)
                        }
                        .clickable { listener.shareFromTrayAction(data.gist.permalink!!) }
                )
            }


        }
    }

}

@Composable
fun VerticalTrayLeftImageTrayItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener,
    isShowDescription: Boolean
) {
    var placeholder: MemoryCache.Key? = null
    var thumbType = layout?.settings?.thumbnailType ?: "landscape"
    // thumbType = "32*9"
    val imageSize = TrayUtil.trayVerticalImageSize(thumbType)
    val imageUrl = TrayUtil.trayImage(thumbType, data.gist?.imageGist!!, imageSize)
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {

        val (thumbImage, iconType, titleText, itemInfo, shareAction) = createRefs()
        val endBarrier = createStartBarrier(shareAction)
        val topBarrier = createTopBarrier(iconType, itemInfo)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            placeholder = painterResource(id = TrayUtil.placeholderMap.get(thumbType)!!),
            error = painterResource(id = TrayUtil.placeholderMap.get(thumbType)!!),
            onSuccess = { placeholder = it.result.memoryCacheKey },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(thumbImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    //height = Dimension.fillToConstraints
                    //end.linkTo(parent.end)

                }
                //.aspectRatio(16f/9f)
                //.width(imageSize.width)
                .size(imageSize)
                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
        )
        Text(
            text = data.gist?.title!!,
            style = TrayUtil.textStyle(layout,"trayItemTitle"),
            textAlign = TextAlign.Start,
            modifier = Modifier
                // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .constrainAs(titleText) {
                    start.linkTo(thumbImage.end, 12.dp)
                    top.linkTo(thumbImage.top, 12.dp)
                    bottom.linkTo(topBarrier)
                    //end.linkTo(parent.end)
                    end.linkTo(endBarrier, 12.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Image(
            painter = painterResource(id = com.viewlift.common.R.drawable.ic_article),
            contentDescription = null,
            modifier = Modifier.constrainAs(iconType) {
                start.linkTo(titleText.start)
                bottom.linkTo(parent.bottom, 12.dp)
            }
        )
        Text(
            text = "2 min read".uppercase(),
            style = TrayUtil.TypoGraphyMap["trayItemTitle"]!!.copy(
                fontSize = 12.sp,
                color = Color(0xff838996)
            ),
            textAlign = TextAlign.Start,
            modifier = Modifier
                // .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .constrainAs(itemInfo) {
                    start.linkTo(iconType.end, 8.dp)

                    //bottom.linkTo(parent.bottom, 12.dp)
                    bottom.linkTo(iconType.bottom)
                    //end.linkTo(parent.end)
                    end.linkTo(parent.end, 12.dp)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (layout?.settings?.enableSharing == false) {

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
            )
        }


    }

}


@Composable
fun HighlightTopImageTrayItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener,
    isShowDescription: Boolean
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val highlightWidth = (screenWidth - 32.dp)
    val highlightHeight = highlightWidth.value / 1.49
    //val highlightHeight = highlightWidth.value / 3.55
    val imageDpSize: DpSize = DpSize(highlightWidth, highlightHeight.dp)
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(TrayUtil.TryaBgColor(layout?.settings), RoundedCornerShape(4.dp))
            .clickable { listener.clickItemTrayAction(data.gist?.id + " -- " + data.gist?.title) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TrayImageTopThumb(topItemData = data, imageDpSize)
        VLTray01ItemText(data, imageDpSize.width, layout, listener)
        /*if (data.gist?.contentType.equals("Playlist",true)){
            VLTrayPlayListItemText(
                data = data,
                trayType = trayType,
                settings = settings
            )
        }*/

         if (isShowDescription && data.gist!=null && !data.gist.description.isNullOrEmpty()) {
             Text(
                 text = data.gist.description.toString(),
                 style = TrayUtil.TypoGraphyMap["traySubTitle"]!!.copy(color = Color(0xffC4CED4)),
                 modifier = Modifier
                     .padding(horizontal = 12.dp)
                     .width(highlightWidth)
             )
         }

        val publishedDate = if(data.publishDate!=null){
            "${UiUtils.getPublishDate(data.publishDate)}"
        } else {
            data.readTime
        }

        val infoText = TrayUtil.getAuthorName(publishedDate, data.author) // Todo add publish time and Author
        Text(
            text = infoText,
            style = TrayUtil.TypoGraphyMap["traySubTitle"]!!.copy(color = Color(0xff838996)),
            modifier = Modifier
                .width(highlightWidth)
                .padding(12.dp)
        )
    }
}

@Composable
fun RelatedTrayItem(
    data: ContentData,
    layout: Layout?,
    listener: TrayActionListener
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val highlightWidth = (screenWidth - 32.dp)
    val highlightHeight = highlightWidth.value / 1.77

    val imageDpSize = DpSize(highlightWidth, highlightHeight.dp)
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(TrayUtil.TryaBgColor(layout?.settings), RoundedCornerShape(4.dp))
            .clickable { listener.clickItemTrayAction(data.gist?.id + " -- " + data.gist?.title) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RelatedTrayImageThumb(
            data = data,
            settings = layout?.settings,
            imageDpSize = imageDpSize,
            listener = listener
        )
        VLTray01ItemText(
            data = data,
            textWidth = imageDpSize.width,
            layout = layout,
            listener = listener
        )

    }
}