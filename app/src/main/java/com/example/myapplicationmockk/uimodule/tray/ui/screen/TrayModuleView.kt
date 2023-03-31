package com.viewlift.uimodule.tray.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.memory.MemoryCache
import com.viewlift.uimodule.button.VLSeeMoreButton


import com.viewlift.uimodule.data.Module
import com.viewlift.uimodule.tray.listener.TrayActionListener
import com.viewlift.uimodule.tray.util.TrayUtil


@Composable
fun TrayModuleView(
    module: Module,
    listener: TrayActionListener
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        TrayHeader(
            title = module.title ?: "",
            settings = module.layout?.settings,
            trayTitleColor = Color.White,
            isDivider = true,
            listener = listener
        )
        LazyRow(contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)) {
            items(items = module.contentData,
                itemContent = {
                    TrayViewItem(
                        data = it,
                        layout = module.layout,
                        listener = listener
                    )
                }
            )
        }
    }
}

/*@Composable
fun TrayModuleView(
    title: String,
    contentDataList: List<ContentData>,
    trayType: String,
    settings: Settings,
    listener: TrayActionListener
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        TrayHeader(
            title = title,
            settings = settings,
            trayTitleColor = Color.White,
            isDivider = true,
            listener = listener
        )
        LazyRow(contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)) {
            items(items = contentDataList,
                itemContent = {
                    TrayViewItem(
                        data = it,
                        layout =
                        trayType = trayType,
                        settings = settings,
                        listener = listener
                    )
                }
            )
        }
    }
}*/



@Composable
fun VLTray01(
    module: Module,
    listener: TrayActionListener
) {
    val settings = module.layout?.settings
    val isParallax = TrayUtil.isParallax(settings)
    val verticalMargin = if (isParallax) 40.dp else 8.dp

    ConstraintLayout(  modifier = Modifier.padding(top = 16.dp)) {
        val (bgParallaxImage,trayContent) = createRefs()
        if (isParallax) {
            val imageUrl = settings?.parallax?.appsParallaxImageUrl
            println(" isParallax $verticalMargin $isParallax  $imageUrl")
            TrayParallaxImage(
                imageUrl = imageUrl,
                resPlaceholder = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!,
                modifier = Modifier.constrainAs(bgParallaxImage){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalMargin)
            .constrainAs(trayContent){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {

            VLTray01Header(
                title = module.title ?: "",
                subtitle = module.subtitle,
                layout = module.layout,
                trayTitleColor = Color.White,
                isDivider = false,
                listener = listener
            )


            LazyRow(contentPadding = PaddingValues(start = 4.dp, end = 4.dp, top = 16.dp, bottom = 4.dp)) {
                items(items = module.contentData,
                    itemContent = {
                        VLTray01ViewItem(
                            data = it,
                            layout = module.layout,
                            listener = listener
                        )
                    }
                )
            }

            if (module.layout?.settings?.showMore == true) {
                VLSeeMoreButton(
                    modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 0.dp)
                    .fillMaxWidth(),module)
            }
        }
    }

}

@Composable
fun VLHighlightTray01(
    module: Module,
    listener: TrayActionListener
) {
    val settings = module.layout?.settings
    val isParallax = TrayUtil.isParallax(settings)
    val verticalMargin = if (isParallax) 40.dp else 8.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val highlightWidth = (screenWidth-32.dp)
    val highlightHeight = highlightWidth.value /1.77
    var placeholder: MemoryCache.Key? = null
    val imageDpSize: DpSize = DpSize(highlightWidth,highlightHeight.dp)
    val topItemData = module.contentData[0]
    val imageUrl = TrayUtil.trayImage("16*9", topItemData.gist?.imageGist!!, imageDpSize)


    //val dataList = module.contentData.subList(1,module.contentData.lastIndex)
    val dataList = TrayUtil.getAvailableData(settings,module.contentData,true)

    ConstraintLayout(modifier = Modifier.padding(top = 16.dp)) {
        val (bgParallaxImage,trayContent) = createRefs()
        if (isParallax) {
            val imageUrl = settings?.parallax?.appsParallaxImageUrl
            println(" isParallax $verticalMargin $isParallax  $imageUrl")
            TrayParallaxImage(
                imageUrl = imageUrl,
                resPlaceholder = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!,
                modifier = Modifier.constrainAs(bgParallaxImage){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalMargin)
                .constrainAs(trayContent){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally) {

            VLTray01Header(
                title = module.title ?: "",
                subtitle = module.subtitle,
                layout = module.layout,
                trayTitleColor = Color.White,
                isDivider = false,
                listener = listener
            )
            Spacer(modifier = Modifier.size(16.dp))
            HighlightTopImageTrayItem(topItemData, module.layout, listener, true)
            Spacer(modifier = Modifier.size(16.dp))
            VLVerticalGrid(dataList = dataList, layout = module.layout, listener = listener)

            if (module.layout?.settings?.showMore == true) {
                VLSeeMoreButton(
                    modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 28.dp)
                    .fillMaxWidth(), module)
                /*Button(
                    onClick = { *//*TODO*//* },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "See all")
                }*/
            }
            //Spacer(modifier = Modifier.size(16.dp))
        }
    }

}

@Composable
fun VLVerticalTray01(
    module: Module,
    listener: TrayActionListener
) {
    val settings = module.layout?.settings
    val isParallax = TrayUtil.isParallax(settings)
    val verticalMargin = if (isParallax) 40.dp else 0.dp



    var placeholder: MemoryCache.Key? = null


    val dataList = TrayUtil.getAvailableData(settings, module.contentData,false)

    ConstraintLayout(modifier = Modifier.padding(top = 16.dp)) {
        val (bgParallaxImage,trayContent) = createRefs()
        if (isParallax) {
            val imageUrl = settings?.parallax?.appsParallaxImageUrl
            println(" isParallax $verticalMargin $isParallax  $imageUrl")
            TrayParallaxImage(
                imageUrl = imageUrl,
                resPlaceholder = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!,
                modifier = Modifier.constrainAs(bgParallaxImage){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalMargin)
                .constrainAs(trayContent){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally) {

            VLTray01Header(
                title = module.title ?: "",
                subtitle = module.subtitle,
                layout = module.layout,
                trayTitleColor = Color.White,
                isDivider = false,
                listener = listener
            )
            Spacer(modifier = Modifier.size(16.dp))
            VLVerticalGrid(dataList = dataList, layout = module.layout, listener = listener)
            if (module.layout?.settings?.showMore == true) {
                VLSeeMoreButton(
                    modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 28.dp)
                    .fillMaxWidth(), module)
            }
            //Spacer(modifier = Modifier.size(16.dp))
        }
    }

}

@Composable
fun VLRelatedVerticalTray01(
    module: Module,
    listener: TrayActionListener
) {
    val settings = module.layout?.settings
    val isParallax = TrayUtil.isParallax(settings)
    val verticalMargin = if (isParallax) 40.dp else 0.dp



    var placeholder: MemoryCache.Key? = null


    val dataList = TrayUtil.getAvailableData(settings, module.contentData,false)

    ConstraintLayout(modifier = Modifier.padding(top = 16.dp)) {
        val (bgParallaxImage,trayContent) = createRefs()
        if (isParallax) {
            val imageUrl = settings?.parallax?.appsParallaxImageUrl
            println(" isParallax $verticalMargin $isParallax  $imageUrl")
            TrayParallaxImage(
                imageUrl = imageUrl,
                resPlaceholder = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!,
                modifier = Modifier.constrainAs(bgParallaxImage){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalMargin)
                .constrainAs(trayContent){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.size(16.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(1), content = {
                items(dataList.size){ index ->
                    RelatedTrayItem(data = dataList[index],
                        layout = module.layout,
                        listener = listener)
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(TrayUtil.gridHeight(module.layout?.settings, dataList.size))
                .height(500.dp)
            )

        }
    }

}