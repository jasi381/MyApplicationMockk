package com.viewlift.uimodule


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.viewlift.uimodule.tray.util.TrayUtil

class UiResBuilder private constructor(
    val r16x9 : Int,
    val r9x16 : Int,
    val r32x9 : Int,
    val r1x1 : Int,
    val r3x4 : Int,
    val r4x3 : Int,
    val portrait : Int,
    val landscape : Int,
    val trayTitle : TextStyle?,
    val traySubTitle : TextStyle?,
    val trayItemTitle : TextStyle?,
    val trayItemInfo : TextStyle?

) {
    private constructor(
         r16x9 : Int,
         r9x16 : Int,
         r32x9 : Int,
         r1x1 : Int,
         r3x4 : Int,
         r4x3 : Int,
         portrait : Int,
         landscape : Int
    ): this( r16x9,r9x16,r32x9,r1x1,r3x4,r4x3, portrait,landscape,null,null,null, null )

    private constructor(
         portrait : Int,
         landscape : Int
    ): this( landscape,portrait,landscape,portrait,portrait,landscape, portrait,landscape,null,null,null,null )

    private constructor(
        resPlaceholder : Int
    ): this( resPlaceholder,resPlaceholder,resPlaceholder,resPlaceholder,resPlaceholder,resPlaceholder, resPlaceholder,resPlaceholder,null,null,null, null )

    private constructor(
        trayTitle : TextStyle?,
        traySubTitle : TextStyle?,
        trayItemTitle : TextStyle?,
        trayItemInfo : TextStyle?
    ): this( 0,0,0,0,0,0,0,0,trayTitle,traySubTitle,trayItemTitle,trayItemInfo )

    private constructor(
        resPlaceholder : Int,
        trayTitle : TextStyle?,
        traySubTitle : TextStyle?,
        trayItemTitle : TextStyle?,
        trayItemInfo : TextStyle?
    ): this( resPlaceholder,resPlaceholder,resPlaceholder,resPlaceholder,resPlaceholder,resPlaceholder, resPlaceholder,resPlaceholder,trayTitle,traySubTitle,trayItemTitle,trayItemInfo )

    var rPlayIcon = 0
    var trayTextColor : Color? = null
    var seeMoreCTA : String? = null
    var fontFamilies:MutableMap<String,FontFamily>? = null

    data class Builder(
        var res16x9: Int = 0,
        var res9x16: Int = 0,
        var res32x9: Int = 0,
        var res1x1: Int = 0,
        var res3x4: Int = 0,
        var res4x3: Int = 0,
        var portrait: Int = 0,
        var landscape: Int = 0,
        var resPlayIcon : Int = 0,
        var seeMoreCTA : String? = null,
        var trayTextColor : Color? = null,
        var trayTitle: TextStyle? = null,
        var traySubTitle: TextStyle? = null,
        var trayItemTitle: TextStyle? = null,
        var trayItemInfo: TextStyle? = null,
        var fontFamilies:MutableMap<String,FontFamily>?=null
        ){

        fun res16x9(res16x9 : Int ) = apply { this.res16x9 = res16x9}
        fun res9x16(res9x16 : Int ) = apply { this.res9x16 = res9x16}
        fun res32x9(res32x9 : Int ) = apply { this.res32x9 = res32x9}
        fun res1x1(res1x1 : Int ) = apply { this.res1x1 = res1x1}
        fun res3x4(res3x4 : Int ) = apply { this.res3x4 = res3x4}
        fun res4x3(res4x3 : Int ) = apply { this.res4x3 = res4x3}
        fun portrait(portrait : Int ) = apply { this.portrait = portrait}
        fun landscape(landscape : Int ) = apply { this.landscape = landscape}
        fun resPlayIcon(resPlayIcon : Int ) = apply { this.resPlayIcon = resPlayIcon}
        fun seeMoreCta(seeMoreCTA: String? ) = apply { this.seeMoreCTA = seeMoreCTA}
        fun trayTextColor(trayTextColor: Color) = apply { this.trayTextColor = trayTextColor}
        fun fontFamilies(fontFamilies: MutableMap<String,FontFamily>?) = apply { this.fontFamilies = fontFamilies}

        fun textStyle(trayTitle : TextStyle, traySubTitle : TextStyle, trayItemTitle : TextStyle, trayItemInfo : TextStyle ) = apply {
            this.trayTitle = trayTitle
            this.traySubTitle = traySubTitle
            this.trayItemTitle = trayItemTitle
            this.trayItemInfo = trayItemInfo
        }
        /*fun trayTitle(trayTitle : TextStyle ) = apply { this.trayTitle = trayTitle}
        fun traySubTitle(traySubTitle : TextStyle ) = apply { this.traySubTitle = traySubTitle}
        fun trayItemTitle(trayItemTitle : TextStyle ) = apply { this.trayItemTitle = trayItemTitle}
        fun trayItemInfo(trayItemInfo : TextStyle ) = apply { this.trayItemInfo = trayItemInfo}*/

        fun placeholderRes(resPlaceholder : Int/*, isOneRes: Boolean*/ ) = apply {
            this.res16x9 = resPlaceholder
            this.res9x16 = resPlaceholder
            this.res32x9 = resPlaceholder
            this.res1x1 = resPlaceholder
            this.res3x4 = resPlaceholder
            this.res4x3 = resPlaceholder
            this.portrait = resPlaceholder
            this.landscape = resPlaceholder
        }

        fun build(): UiResBuilder {
            var uiResBuilder : UiResBuilder
            if (!isTextStyle()) {
                uiResBuilder = UiResBuilder(
                    res16x9,
                    res9x16,
                    res32x9,
                    res1x1,
                    res3x4,
                    res4x3,
                    portrait,
                    landscape
                )
            }else if (!isPlaceholder()){
                uiResBuilder = UiResBuilder(
                    trayTitle,
                    traySubTitle,
                    trayItemTitle,
                    trayItemInfo
                )
            } else {
                uiResBuilder = UiResBuilder(
                    res16x9,
                    res9x16,
                    res32x9,
                    res1x1,
                    res3x4,
                    res4x3,
                    portrait,
                    landscape,
                    trayTitle,
                    traySubTitle,
                    trayItemTitle,
                    trayItemInfo
                )
            }
            if (resPlayIcon != 0){
                uiResBuilder.rPlayIcon = resPlayIcon
            }
            trayTextColor.let {
                uiResBuilder.trayTextColor = it
            }
            seeMoreCTA.let {
                uiResBuilder.seeMoreCTA = it
            }
            fontFamilies?.let {
                uiResBuilder.fontFamilies = it
            }

            return uiResBuilder
        }

        fun isTextStyle(): Boolean {
            return  if (trayTitle == null || traySubTitle == null || trayItemTitle == null|| trayItemInfo == null) false else true
        }
        fun isPlaceholder(): Boolean {
            return  if (res16x9 ==0 ||res9x16 ==0 ||res32x9 ==0 ||res1x1 ==0 || res3x4 ==0 ||res4x3 ==0 ||portrait ==0 || landscape ==0) false else true
        }
    }

    fun setupRes(){
        if (r16x9 ==0 ||r9x16 ==0 ||r32x9 ==0 ||r1x1 ==0 || r3x4 ==0 ||r4x3 ==0 ||portrait ==0 || landscape ==0){

        }else {
            TrayUtil.placeholderMap.put("16*9",r16x9)
            TrayUtil.placeholderMap.put("9*16",r9x16)
            TrayUtil.placeholderMap.put("32*9",r32x9)
            TrayUtil.placeholderMap.put("1*1",r1x1)
            TrayUtil.placeholderMap.put("3*4",r3x4)
            TrayUtil.placeholderMap.put("4*3",r4x3)
            TrayUtil.placeholderMap.put("portrait",portrait)
            TrayUtil.placeholderMap.put("landscape",landscape)
        }

        trayTextColor?.let {
            TrayUtil.trayTextColor = it
        }

        seeMoreCTA?.let {
            TrayUtil.seeMoreCTA = it
        }
        if (rPlayIcon != 0 ){
            TrayUtil.playIconRes = rPlayIcon
        }
        fontFamilies?.let {
            it.forEach{ entry ->
                TrayUtil.FontFamilyMap[entry.key] = entry.value
            }
        }
    }

}