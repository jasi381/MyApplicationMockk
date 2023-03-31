package com.viewlift.uimodule.tray.util


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.viewlift.uimodule.R
import com.viewlift.uimodule.data.*


object TrayUtil {

    fun getAuthorName(readTime: String?, author: String?): String {
        return if(!readTime.isNullOrEmpty() && !author.isNullOrEmpty()){
            "$readTime • $author"
        } else if (readTime.isNullOrEmpty() && !author.isNullOrEmpty()){
            author
        } else if (!readTime.isNullOrEmpty() && author.isNullOrEmpty() ) {
            readTime
        } else {
            ""
        }
    }

    const val MINUTES_IN_AN_HOUR = 60
    const val SECONDS_IN_A_MINUTE = 60

    val isTablet = false

    val trayTitle= TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    val traySubTitle= TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    val trayItemTitle= TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
    val trayItemInfo= TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    )
    val trayItemTeam= TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    )



    var playIconRes = R.drawable.icon_play

    var trayBgColor = Color.Blue
    var trayTextColor = Color.White
    var seeMoreCTA = "See More"

    val TypoGraphyMap = mutableMapOf<String,TextStyle>(
        "trayTitle" to trayTitle,
        "traySubTitle" to traySubTitle,
        "trayItemTitle" to trayItemTitle,
        "trayItemInfo" to trayItemInfo,
        "team" to trayItemTeam
    )

    val placeholderMap = mutableMapOf<String,Int>(
        "16*9" to R.drawable.image_placeholder,
        "1*1" to R.drawable.image_placeholder,
        "9*16" to R.drawable.image_placeholder,
        "3*4" to R.drawable.image_placeholder,
        "4*3" to R.drawable.image_placeholder,
        "32*9" to R.drawable.image_placeholder,
        "portrait" to R.drawable.image_placeholder,
        "landscape" to R.drawable.image_placeholder,
        "" to R.drawable.image_placeholder,
        "null" to R.drawable.image_placeholder
    )

    val FontFamilyMap = mutableMapOf<String, FontFamily>()

    fun textStyle(layout: Layout?, key: String): TextStyle{
        var style = TypoGraphyMap[key]!!
        layout?.fontSetting?.let {
            val fontSize = getFontSize(key, it)
            style = style.copy(fontSize = fontSize.sp)
            val fontFamily = setFontFromFontStyle(key, it)
            style = style.copy(fontFamily = fontFamily)
            style = setFontWeight(key, it, style)
        }
        return style
    }

    private fun getFontSize(key: String, fontSetting: FontsSetting) : Int{
        return when(key){
            "trayTitle"-> evaluateFontSize(fontSetting.titleFontSize,key)
            "traySubTitle"-> evaluateFontSize(fontSetting.subtitleFontSize,key)
            "trayItemTitle"-> evaluateFontSize(fontSetting.trayItemTitleFontSize,key)
            "trayItemSubtitle"-> evaluateFontSize(fontSetting.trayItemSubtitleFontSize,key)
            "trayButton"-> evaluateFontSize(fontSetting.buttonFontSize,key)
            "team"-> evaluateFontSize(fontSetting.teamFontSize,key)
            else -> evaluateFontSize(fontSetting.trayItemTitleFontSize,key)
        }
    }

    private fun evaluateFontSize(fontSize: String?, key: String) = if (fontSize.isNullOrEmpty()) {
        getDefaultFontSize(key)
    } else {
        if(!fontSize.contains("px"))
        fontSize.toInt()
        else getDefaultFontSize(key)
    }


    private fun setFontWeight(key: String, fontsSetting: FontsSetting, style: TextStyle) : TextStyle {
         when(key){
            "trayTitle"-> {
                fontsSetting.titleFontWeight?.let{
                    style.copy(fontWeight = getFontWeight(it))
                }
            }
             "traySubTitle"-> {
                 fontsSetting.subtitleFontWeight?.let{
                     style.copy(fontWeight = getFontWeight(it))
                 }
             }
             "trayItemTitle"-> {
                 fontsSetting.trayItemTitleFontWeight?.let{
                     style.copy(fontWeight = getFontWeight(it))
                 }
             }
             "trayItemSubtitle"-> {
                 fontsSetting.trayItemFontWeight?.let{
                     style.copy(fontWeight = getFontWeight(it))
                 }
             }
             "trayButton"-> {
                 fontsSetting.buttonFontWeight?.let{
                     style.copy(fontWeight = getFontWeight(it))
                 }
             }
             "team"-> {
                 fontsSetting.teamFontWeight?.let{
                     style.copy(fontWeight = getFontWeight(it))
                 }
             }
        }
        return style
    }

    private fun setFontFromFontStyle(key: String, fontsSetting: FontsSetting) : FontFamily? {
         when(key){
            "trayTitle"-> {
                fontsSetting.titleFontFamilyName?.let{
                        return getFontFamily(it)
                }
            }
             "traySubTitle"-> {
                 fontsSetting.subtitleFontFamilyName?.let{
                     return getFontFamily(it)
                 }
             }
             "trayItemTitle"-> {
                 fontsSetting.trayItemTitleFontFamilyName?.let{
                     return getFontFamily(it)
                 }
             }
             "trayItemSubtitle"-> {
                 fontsSetting.subtitleFontFamilyName?.let{
                     return getFontFamily(it)
                 }
             }
             "trayButton"-> {
                 fontsSetting.buttonFontFamilyName?.let{
                     return getFontFamily(it)
                 }
             }
             "team"-> {
                 fontsSetting.teamFontFamilyName?.let{
                     return getFontFamily(it)
                 }
             }
        }
        return null
    }

    private fun getFontWeight(weight: String): FontWeight {
        return when(weight){
            "bold"->FontWeight.Bold
            "semiBold"->FontWeight.SemiBold
            "light"->FontWeight.Light
            "medium"->FontWeight.Medium
            "thin"->FontWeight.Thin
            "extraLight"->FontWeight.ExtraLight
            "extraBold"->FontWeight.ExtraBold
            else->FontWeight.Normal

        }
    }

    private fun getDefaultFontSize(key: String) : Int{
        return when(key){
            "trayTitle"-> 18
            "traySubTitle"-> 12
            "trayItemTitle"-> 14
            "trayItemSubtitle"-> 12
            "trayButton"-> 12
            "team"-> 28
            else -> 12
        }
    }
    private fun getFontFamily(fontName : String?):FontFamily{
        return FontFamilyMap[fontName]?: FontFamily.Default
    }

    fun tryType(value : String): TrayType {

        return when {
            value.equals(TrayType.CIRCLE.name, true) -> TrayType.CIRCLE
            value.equals(TrayType.SQUIRE.name, true) -> TrayType.SQUIRE
            value.equals(TrayType.RECTANGLE16x9.name, true) -> TrayType.RECTANGLE16x9
            value.equals(TrayType.RECTANGLE3x4.name, true) -> TrayType.RECTANGLE3x4
            value.equals(TrayType.tray01.name, true) -> TrayType.tray01
            value.equals(TrayType.tray02.name, true) -> TrayType.tray02
            value.equals(TrayType.tray03.name, true) -> TrayType.tray03
            value.equals(TrayType.tray04.name, true) -> TrayType.tray04
            value.equals(TrayType.tray05.name, true) -> TrayType.tray05
            value.equals(TrayType.tray06.name, true) -> TrayType.tray06
            value.equals(TrayType.tray07.name, true) -> TrayType.tray07
            value.equals(TrayType.newsTray02.name, true) -> TrayType.newsTray02
            else -> TrayType.RECTANGLE16x9
        }
    }

    fun trayImageSize(value: String) : DpSize{
        return when {
            value.equals(TrayType.CIRCLE.name, true) -> DpSize(128.dp, 128.dp)
            value.equals(TrayType.SQUIRE.name, true) -> DpSize(180.dp, 180.dp)
            value.equals(TrayType.RECTANGLE16x9.name, true) -> DpSize(320.dp, 180.dp)
            value.equals(TrayType.RECTANGLE3x4.name, true) -> DpSize(240.dp, 320.dp)
            value.equals(TrayType.tray01.name, true) -> DpSize(240.dp, 320.dp)
            value.equals(TrayType.tray03.name, true) -> DpSize(208.dp, 117.dp)
            value.equals(TrayType.tray05.name, true) -> DpSize(240.dp, 135.dp)
            value.equals(TrayType.tray02.name, true) -> DpSize(320.dp, 180.dp)
            value.equals(TrayType.tray04.name, true) -> DpSize(320.dp, 180.dp)
            value.equals(TrayType.tray06.name, true) -> DpSize(208.dp, 117.dp)
            value.equals(TrayType.tray07.name, true) -> DpSize(180.dp, 320.dp)
            value.equals(TrayType.newsTray02.name, true) -> DpSize(128.dp, 128.dp)
            value.equals("16*9", true) -> DpSize(283.dp, 159.dp)
            value.equals("3*4", true) -> DpSize(240.dp, 320.dp)
            value.equals("4*3", true) -> DpSize(320.dp, 240.dp)
            value.equals("1*1", true) -> DpSize(132.dp, 132.dp)
            value.equals("32*9", true) -> DpSize(640.dp, 180.dp)
            value.equals("9*16", true) -> DpSize(283.dp, 423.dp)
            value.equals("portrait", true) -> DpSize(240.dp, 320.dp)
            value.equals("landscape", true) -> DpSize(320.dp, 180.dp)
            else -> DpSize(320.dp, 180.dp)
        }
    }
    fun trayVerticalImageSize(value: String) : DpSize {
        return when {
            value.equals("16*9", true) -> DpSize(183.dp, 102.dp)
            value.equals("3*4", true) -> DpSize(153.dp, 204.dp)
            value.equals("4*3", true) -> DpSize(204.dp, 153.dp)
            value.equals("1*1", true) -> DpSize(133.dp, 133.dp)
            value.equals("32*9", true) -> DpSize(363.dp, 180.dp)
            value.equals("9*16", true) -> DpSize(102.dp, 183.dp)
            value.equals("portrait", true) -> DpSize(153.dp, 204.dp)
            value.equals("landscape", true) -> DpSize(183.dp, 102.dp)
            else -> DpSize(183.dp, 102.dp)
        }
    }

    fun getAvailableData(settings : Settings?, dataList : List<ContentData>, isHihglight : Boolean) :  List<ContentData>{
        val itemCount = verticalTrayVisibleItemsCount(settings,dataList.size)
        var startIndex = 0
        if (isHihglight){
            startIndex = 1
        }
        return dataList.subList(startIndex, itemCount)
    }

    fun verticalTrayVisibleItemsCount(settings : Settings?, dataSize : Int) : Int{
        val numberOfItemVisible = Math.ceil(settings?.columns?.mobile!!.toDouble()).toInt()
        return if (dataSize< numberOfItemVisible) dataSize else numberOfItemVisible
    }

    fun gridHeight(settings : Settings?, dataSize : Int) :Dp{
        var thumbType = settings?.thumbnailType!!
        //thumbType = "16*9"
        var numberOfItemVisible = Math.ceil(settings?.columns?.mobile!!.toDouble()).toInt()
        numberOfItemVisible = if (dataSize< numberOfItemVisible) dataSize else numberOfItemVisible
        val thumbHeight = trayVerticalImageSize(thumbType).height +20.dp
        return (numberOfItemVisible * thumbHeight.value).dp
    }

    fun trayTextWidth(value: String) : Dp{
        return when {
            value.equals(TrayType.CIRCLE.name, true) -> 128.dp
            value.equals(TrayType.SQUIRE.name, true) -> 180.dp
            value.equals(TrayType.RECTANGLE16x9.name, true) -> 320.dp
            value.equals(TrayType.RECTANGLE3x4.name, true) -> 240.dp
            value.equals(TrayType.tray01.name, true) -> 240.dp
            value.equals(TrayType.tray03.name, true) -> 208.dp
            value.equals(TrayType.tray05.name, true) -> 240.dp
            value.equals(TrayType.tray02.name, true) -> 320.dp
            value.equals(TrayType.tray04.name, true) -> 320.dp
            value.equals(TrayType.tray06.name, true) -> 208.dp
            value.equals(TrayType.tray07.name, true) -> 180.dp
            value.equals(TrayType.newsTray02.name, true) -> 128.dp
            value.equals("16*9", true) -> 283.dp
            value.equals("3*4", true) -> 240.dp
            value.equals("4*3", true) ->320.dp
            value.equals("1*1", true) -> 132.dp
            value.equals("32*9", true) -> 640.dp
            value.equals("9*16", true) -> 283.dp
            value.equals("portrait", true) -> 240.dp
            value.equals("landscape", true) -> 320.dp
            else -> 320.dp
        }
    }
    fun trayImage(value: String, imageGist: ImageGist, dpSize: DpSize) : String{
        var imageUrl = when {
            value.equals(TrayType.CIRCLE.name, true) -> imageGist.r1x1
            value.equals(TrayType.SQUIRE.name, true) -> imageGist.r1x1
            value.equals(TrayType.RECTANGLE16x9.name, true) -> imageGist.r16x9
            value.equals(TrayType.RECTANGLE3x4.name, true) -> imageGist.r3x4
            value.equals(TrayType.tray01.name, true) -> imageGist.r3x4
            value.equals(TrayType.tray02.name, true) -> imageGist.r16x9
            value.equals(TrayType.tray03.name, true) -> imageGist.r16x9
            value.equals(TrayType.tray04.name, true) -> imageGist.r16x9
            value.equals(TrayType.tray05.name, true) -> imageGist.r16x9
            value.equals(TrayType.tray06.name, true) -> imageGist.r16x9
            value.equals(TrayType.tray07.name, true) -> imageGist.r9x16
            value.equals(TrayType.newsTray02.name, true) -> imageGist.r1x1
            value.equals("16*9", true) -> imageGist.r16x9
            value.equals("3*4", true) -> imageGist.r3x4//?:imageGist.r16x9
            value.equals("4*3", true) ->imageGist.r4x3//?:imageGist.r16x9
            value.equals("1*1", true) -> imageGist.r1x1//?:imageGist.r16x9
            value.equals("32*9", true) -> imageGist.r32x9//?:imageGist.r16x9
            value.equals("9*16", true) -> imageGist.r9x16//?:imageGist.r16x9
            value.equals("portrait", true) -> imageGist.r3x4//?:imageGist.r16x9
            value.equals("landscape", true) -> imageGist.r16x9//?:imageGist.r16x9
            else -> imageGist.r16x9
        }
       // imageUrl =imageGist.r16x9
        return imageUrl.plus("?impolicy=resize&w=${dpSize.width.value}&h=${dpSize.height.value}")
    }

    fun trayImage(value: String, badgeImages: BadgeImages, dpSize: DpSize) : String{
        val imageUrl = when {
            value.equals(TrayType.CIRCLE.name, true) -> badgeImages.r1x1
            value.equals(TrayType.SQUIRE.name, true) -> badgeImages.r1x1
            value.equals(TrayType.RECTANGLE16x9.name, true) -> badgeImages.r16x9
            value.equals(TrayType.RECTANGLE3x4.name, true) -> badgeImages.r3x4
            value.equals(TrayType.tray01.name, true) -> badgeImages.r3x4
            value.equals(TrayType.tray02.name, true) -> badgeImages.r16x9
            value.equals(TrayType.tray03.name, true) -> badgeImages.r16x9
            value.equals(TrayType.tray04.name, true) -> badgeImages.r16x9
            value.equals(TrayType.tray05.name, true) -> badgeImages.r16x9
            value.equals(TrayType.tray06.name, true) -> badgeImages.r16x9
            value.equals(TrayType.tray07.name, true) -> badgeImages.r9x16
            value.equals(TrayType.newsTray02.name, true) -> badgeImages.r1x1
            value.equals("16*9", true) -> badgeImages.r16x9
            value.equals("3*4", true) -> badgeImages.r3x4
            value.equals("4*3", true) -> badgeImages.r4x3
            value.equals("1*1", true) -> badgeImages.r1x1
            value.equals("32*9", true) -> badgeImages.r32x9
            value.equals("9*16", true) -> badgeImages.r9x16
            value.equals("portrait", true) -> badgeImages.r3x4
            value.equals("landscape", true) -> badgeImages.r16x9
            else -> badgeImages.r16x9
        }

        return imageUrl.plus("?impolicy=resize&w=${dpSize.width.value}&h=${dpSize.height.value}")
    }

    fun isRemoveDescription(settings: Settings?) : Boolean{
        return settings != null && settings.enableOverrideSettings && settings.removeTrayBottomTitle
    }

    fun isRoundedCorner(settings: Settings?) : Boolean{
        return settings != null && settings.enableOverrideSettings && settings.roundedCornerButton
    }
    fun isComplexHeader(settings: Settings?) : Boolean{
        return (settings != null &&  (settings.seeAll || settings.seeAllCard || settings.showMore || settings.trayIconUrl != null) && (settings.seeAllPermalink!= null && settings.seeAllPermalink.isNotEmpty()))
    }

    fun getRuntime(totalSeconds: Int): String {
        //val seconds = totalSeconds % SECONDS_IN_A_MINUTE
        //val totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE
        //val minutes = totalMinutes % MINUTES_IN_AN_HOUR
        //val hours = totalMinutes / MINUTES_IN_AN_HOUR
        //return "$hours : $minutes : $seconds"
        return getHourFromSecond(totalSeconds).plus(getMinuteFromSecond(totalSeconds).plus(getSecondFromTotalSecond(totalSeconds)))
    }

    fun getHourFromSecond(totalSeconds: Int): String {
        val totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE
        val hours = totalMinutes / MINUTES_IN_AN_HOUR
        return if (hours >0) "$hours:" else ""
    }
    fun getMinuteFromSecond(totalSeconds: Int): String {
        val totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE
        val minutes = totalMinutes % MINUTES_IN_AN_HOUR
        return if (minutes >0) "$minutes:" else ""
    }
    fun getSecondFromTotalSecond(totalSeconds: Int): String {
        val seconds = totalSeconds % SECONDS_IN_A_MINUTE
        return if (seconds >0) "$seconds" else "00"
    }

    fun isParallax(settings: Settings?) : Boolean {
        return (settings != null && settings?.parallax != null && settings?.parallax?.enable == true )
    }
    fun parallaxImageUrl(settings: Settings?) : String? {
        return  settings?.parallax?.imageUrl
    }

    fun TryaBgColor(settings: Settings?) :Color{
        return if (settings?.textBackgroundColor != null && settings?.textBackgroundColor.r.toInt() > 0)

            Color(settings.textBackgroundColor.r.toInt(),
                settings.textBackgroundColor.g.toInt(),
                settings.textBackgroundColor.b.toInt(),
                //settings.textBackgroundColor.a.toInt()
            )
            else if (settings?.textBackgroundColor != null && settings?.textBackgroundColor.r.toFloat() > 0.0f)
            //Color(0.14f,0.34f,0.65f, 0.8f)
            Color(settings.textBackgroundColor.r,
                settings.textBackgroundColor.g,
                settings.textBackgroundColor.b,
                //settings.textBackgroundColor.a.toInt()
            )
         else Color.Transparent
    }

    fun TryaButtonBorderColor(settings: Settings?) :Color{
        return if (settings?.textBackgroundColor != null && settings?.textBackgroundColor.r.toInt() > 0)

            Color(settings.textBackgroundColor.r.toInt(),
                settings.textBackgroundColor.g.toInt(),
                settings.textBackgroundColor.b.toInt(),
                //settings.textBackgroundColor.a.toInt()
            )
            else if (settings?.textBackgroundColor != null && settings?.textBackgroundColor.r.toFloat() > 0.0f)
            //Color(0.14f,0.34f,0.65f, 0.8f)
            Color(settings.textBackgroundColor.r,
                settings.textBackgroundColor.g,
                settings.textBackgroundColor.b,
                //settings.textBackgroundColor.a.toInt()
            )
         else Color(0x29000000+trayTextColor.toArgb())
    }

    fun getCalculatedHeight(value: String?, screenWidth: Int): Int? {
        val totalArea = value?.split("*")
        val width = totalArea?.get(0) // 16
        val height = totalArea?.get(1) // 9

        return if(width!=null && height!=null) {
            (screenWidth / width.toInt()) * height.toInt()
        } else {
            null
        }
    }

    fun getOrientationType(
        imageGist: ImageGist? = null,
        thumbnailType: String? = null // default value
    ): String? {
        return when {
            thumbnailType.equals("16*9", true) -> imageGist?.r16x9
            thumbnailType.equals("3*4", true) -> imageGist?.r3x4
            thumbnailType.equals("4*3", true) -> imageGist?.r4x3
            thumbnailType.equals("1*1", true) -> imageGist?.r1x1
            thumbnailType.equals("32*9", true) -> imageGist?.r32x9
            thumbnailType.equals("9*16", true) -> imageGist?.r9x16
            else -> getNonNullImage(imageGist)
        }
    }

    fun getNonNullImage(imageGist: ImageGist? = null,): String? {
        return if (imageGist?.r16x9 != null){
            imageGist.r16x9
        } else if (imageGist?.r3x4 != null){
            imageGist.r3x4
        } else if (imageGist?.r4x3 != null){
            imageGist.r4x3
        } else if (imageGist?.r1x1 != null){
            imageGist.r1x1
        } else if (imageGist?.r32x9 != null){
            imageGist.r32x9
        } else imageGist?.r9x16
    }

    fun getOrientationType(
        imageGist: com.viewlift.network.fragment.PartialGist.ImageGist? = null,
        thumbnailType: String? = "16*9" // default value
    ): String? {
        return when {
            thumbnailType.equals("16*9", true) -> imageGist?.r16x9
            thumbnailType.equals("3*4", true) -> imageGist?.r3x4
            thumbnailType.equals("4*3", true) -> imageGist?.r4x3
            thumbnailType.equals("1*1", true) -> imageGist?.r1x1
            thumbnailType.equals("32*9", true) -> imageGist?.r32x9
            thumbnailType.equals("9*16", true) -> imageGist?.r9x16
            else -> imageGist?.r16x9
        }
    }
}