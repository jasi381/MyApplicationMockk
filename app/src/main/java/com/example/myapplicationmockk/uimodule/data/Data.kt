package com.viewlift.uimodule.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.viewlift.network.data.remote.model.response.GameScore
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep @Parcelize
data class Module(
    @SerializedName("contentData")
    @Expose
    val contentData: List<ContentData>,
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("layout")
    @Expose
    val layout: Layout?,
    @SerializedName("moduleType")
    @Expose
    val moduleType: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("subtitle")
    @Expose
    val subtitle: String?,
    @SerializedName("metadataMap")
    @Expose
    val metadataMap: LinkedHashMap<String?, String?>? = null,
) : Parcelable

@Keep @Parcelize
data class ContentData(
    @SerializedName("gist")
    @Expose
    val gist: Gist?,
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("parentalRating")
    @Expose
    val parentalRating: String?,
    @SerializedName("runtime")
    @Expose
    val runtime: Int,
    @SerializedName("readTime")
    @Expose
    val readTime: String? = null,
    @SerializedName("contentType")
    @Expose
    val contentType: String? = null,
    @SerializedName("author")
    @Expose
    val author: String? = null,
    @SerializedName("homeTeam")
    @Expose
    val homeTeam: Team? = null,
    @SerializedName("awayTeam")
    @Expose
    val awayTeam: Team? = null,
    @SerializedName("highlights")
    @Expose
    val highlights: ArrayList<Highlights> = arrayListOf(),
    @SerializedName("livestreams")
    @Expose
    val livestreams: ArrayList<Highlights> = arrayListOf(),
    @SerializedName("preview")
    @Expose
    val preview: Highlights? = null,
    @SerializedName("currentState")
    @Expose
    val currentState: String? = null,
    @SerialName("states")
    var states: GameState? = null,
    @SerialName("metadata")
    var metadata: ArrayList<Metadata> = arrayListOf(),
    @SerialName("schedules")
    var schedules: ArrayList<ScheduleEvent> = arrayListOf(),
    @SerializedName("broadcaster")
    @Expose
    val broadcaster: String? = null,
    @SerializedName("score")
    @Expose
    var score: GameScores? = null,
    @SerializedName("publishDate")
    @Expose
    val publishDate: Long? = null,

) :Parcelable

@Keep
@Parcelize
data class GameScores(
    @SerialName("status") var status: String? = null,
    @SerialName("type") var type: String? = null,
    @SerialName("number") var number: Int? = null,
    @SerialName("homePoint") var homePoint: Int? = null,
    @SerialName("awayPoint") var awayPoint: Int? = null,
): Parcelable


@Keep
@Parcelize
data class ScheduleEvent(
    @SerialName("startDate")val startDate: Long?,
    @SerialName("endDate")val endDate: Long?,
    @SerialName("venue")val venue: Venue?,
): Parcelable


@Keep
@Parcelize
data class Venue(
    @SerialName("id")val id: String?,
    @SerialName("title")val title: String?,
): Parcelable

@Keep
@Parcelize
data class Metadata(
    @SerialName("name")val name: String?,
    @SerialName("value")val value : String?,
): Parcelable

@Keep
@Parcelize
data class GameState(
    @SerialName("default")val default: GameStatesDetails?,
    @SerialName("pre")val pre: GameStatesDetails?,
    @SerialName("live")val live: GameStatesDetails?,
    @SerialName("post")val post: GameStatesDetails?,
    @SerialName("end")val end: GameStatesDetails?,
): Parcelable

@Keep
@Parcelize
data class GameStatesDetails(
    @SerialName("startDateTime")val startDateTime: Long?,
    @SerialName("endDateTime")val endDateTime: Long?,
): Parcelable

@Keep @Parcelize
data class Highlights(
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("gist")
    @Expose
    val gist: Gist?,

): Parcelable


@Keep @Parcelize
data class Team(
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("shortName")
    @Expose
    val shortName: String?,
    @SerializedName("gist")
    @Expose
    val gist: Gist?,

) :Parcelable

@Keep @Parcelize
data class Layout(
    @SerializedName("blockName")
    @Expose
    val blockName: String? = null,
    @SerializedName("id")
    @Expose
    val id: String? = null,
    @SerializedName("isMasthead")
    @Expose
    val isMasthead: Boolean? = false,
    @SerializedName("settings")
    @Expose
    val settings: Settings? = null,
    @SerializedName("styles")
    @Expose
    val styles: Styles? = null,
    @SerializedName("type")
    @Expose
    val type: String? = null,
    @SerializedName("view")
    @Expose
    val view: String?,
    @SerializedName("fontSetting")
    @Expose
    val fontSetting: FontsSetting?=null
) :Parcelable

@Keep @Parcelize
data class Gist(
    @SerializedName("contentType")
    @Expose
    val contentType: String?,
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("imageGist")
    @Expose
    val imageGist: ImageGist?,
    @SerializedName("permalink")
    @Expose
    val permalink: String?,

    @SerializedName("title")
    @Expose
    val title: String?,

    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("badgeImages")
    @Expose
    val badgeImages: BadgeImages?,
) :Parcelable

@Keep @Parcelize
data class ImageGist(
    @SerializedName("r16x9")
    @Expose
    val r16x9: String?,
    @SerializedName("r1x1")
    @Expose
    val r1x1: String?,
    @SerializedName("r32x9")
    @Expose
    val r32x9: String?,
    @SerializedName("r3x4")
    @Expose
    val r3x4: String?,
    @SerializedName("r4x3")
    @Expose
    val r4x3: String?,
    @SerializedName("r9x16")
    @Expose
    val r9x16: String?
) : Parcelable

@Keep @Parcelize
data class BadgeImages(
    @SerializedName("r16x9")
    @Expose
    val r16x9: String?,
    @SerializedName("r1x1")
    @Expose
    val r1x1: String?,
    @SerializedName("r32x9")
    @Expose
    val r32x9: String?,
    @SerializedName("r3x4")
    @Expose
    val r3x4: String?,
    @SerializedName("r4x3")
    @Expose
    val r4x3: String?,
    @SerializedName("r9x16")
    @Expose
    val r9x16: String?
): Parcelable

@Keep @Parcelize
data class Settings(
//    @SerializedName("fontSettings")
//    @Expose
//    val fontSettings: List<FontSettings>? = null,
    @SerializedName("columns")
    @Expose
    val columns: Columns,

    @SerializedName("loop")
    @Expose
    val loop: Boolean = false,
    @SerializedName("enableSharing")
    @Expose
    val enableSharing: Boolean = false,
    @SerializedName("parallax")
    @Expose
    val parallax: Parallax,
    @SerializedName("showContentDuration")
    @Expose
    val showContentDuration: Boolean = false,
    @SerializedName("textBackgroundColor")
    @Expose
    val textBackgroundColor: TextBackgroundColor,
    @SerializedName("thumbnailPlacement")
    @Expose
    val thumbnailPlacement: String,
    @SerializedName("thumbnailType")
    @Expose
    val thumbnailType: String,
    @SerializedName("trayIconUrl")
    @Expose
    val trayIconUrl: String?,
    @SerializedName("fontSettings")
    @Expose
    val fontSettings: List<FontSetting>?,



    @SerializedName("seeAllPermalink")@Expose val seeAllPermalink: String?,
    @SerializedName("showMoreCTA")@Expose val showMoreCTA: String,
    @SerializedName("seeAll")@Expose val seeAll: Boolean,
    @SerializedName("seeAllCard")@Expose val seeAllCard: Boolean,
    @SerializedName("showMore")@Expose val showMore: Boolean,
    @SerializedName("showMorePermalink")@Expose val showMorePermalink: String?,
    @SerializedName("enableCustomStyle")@Expose val enableCustomStyle: Boolean,
    @SerializedName("enableOverrideSettings")@Expose val enableOverrideSettings: Boolean,
    @SerializedName("roundedCornerButton")@Expose val roundedCornerButton: Boolean,
    @SerializedName("removeTrayBottomTitle")@Expose val removeTrayBottomTitle: Boolean,
    // Schedule Tray Items
    @SerializedName("defaultGameColor")@Expose var defaultGameColor: String? = null,
    @SerializedName("liveGameColor")@Expose var liveGameColor: String? = null,
    @SerializedName("moduleBackgroundColor")@Expose var moduleBackgroundColor: String? = null,
    @SerializedName("postGameColor")@Expose var postGameColor: String? = null,
    @SerializedName("preGameColor")@Expose var preGameColor: String? = null,
    @SerializedName("trayTitleColor")@Expose var trayTitleColor: String? = null,
    @SerializedName("calandarBgColor")@Expose var calandarBgColor: String? = null

): Parcelable

@Keep
@Parcelize
data class FontSettings(
    @SerializedName("fontfamilyName") var fontfamilyName: String? = null,
    @SerializedName("fontfamilyUrl") var fontfamilyUrl: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("subtitleFontSize") var subtitleFontSize: String? = null,
    @SerializedName("subtitleFontWeight") var subtitleFontWeight: String? = null,
    @SerializedName("subtitleFontfamilyName") var subtitleFontfamilyName: String? = null,
    @SerializedName("titleFontSize") var titleFontSize: String? = null,
    @SerializedName("titleFontWeight") var titleFontWeight: String? = null,
    @SerializedName("titleFontfamilyName") var titleFontfamilyName: String? = null,
    @SerializedName("trayItemFontSize") var trayItemFontSize: String? = null,
    @SerializedName("trayItemFontWeight") var trayItemFontWeight: String? = null,
    @SerializedName("trayItemFontfamilyName") var trayItemFontfamilyName: String? = null,
    @SerializedName("trayItemSubtitleFontSize") var trayItemSubtitleFontSize: String? = null,
    @SerializedName("type") var type: String? = null
) : Parcelable

@Keep @Parcelize
data class Columns(
    @SerializedName("desktop")
    @Expose
    val desktop: Double,
    @SerializedName("mobile")
    @Expose
    val mobile: Double,
    @SerializedName("ott")
    @Expose
    val ott: Double,
    @SerializedName("tablet")
    @Expose
    val tablet: Double
): Parcelable

@Keep @Parcelize
data class Parallax(
    @SerializedName("enable")
    @Expose
    val enable: Boolean = false,
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String?,
    @SerializedName("appsParallaxImageUrl")
    @Expose
    val appsParallaxImageUrl: String?
) :Parcelable

@Keep @Parcelize
data class TextBackgroundColor(
    @SerializedName("a")
    @Expose
    val a: Float,
    @SerializedName("b")
    @Expose
    val b: Float,
    @SerializedName("g")
    @Expose
    val g: Float,
    @SerializedName("r")
    @Expose
    val r: Float
) :Parcelable

@Keep @Parcelize
data class Styles (
    val bgcolor :String?
        ): Parcelable

@Keep @Parcelize
data class FontSetting(
    @SerializedName("fontfamilyName")
    @Expose
    val fontfamilyName : String?,
    @SerializedName("fontfamilyUrl")
    @Expose
    val fontfamilyUrl : String?,
    @SerializedName("id")
    @Expose
    val id : String?,
    @SerializedName("subtitleFontSize")
    @Expose
    val subtitleFontSize : String?,
    @SerializedName("titleFontSize")
    @Expose
    val titleFontSize : String?,
    @SerializedName("trayItemFontSize")
    @Expose
    val trayItemFontSize : String?,
    @SerializedName("type")
    @Expose
    val type : String?
): Parcelable

@Keep @Parcelize
data class FontsSetting(
    @SerializedName("titleFontFamilyName")
    @Expose
    val titleFontFamilyName : String?,

    @SerializedName("titleFontSize")
    @Expose
    val titleFontSize : String?,

    @SerializedName("titleFontWeight")
    @Expose
    val titleFontWeight : String?,

    @SerializedName("subtitleFontFamilyName")
    @Expose
    val subtitleFontFamilyName : String?,

    @SerializedName("subtitleFontSize")
    @Expose
    val subtitleFontSize : String?,

    @SerializedName("subtitleFontWeight")
    @Expose
    val subtitleFontWeight : String?,

    @SerializedName("trayItemFontFamilyName")
    @Expose
    val trayItemFontFamilyName : String?,

    @SerializedName("trayItemSubtitleFontSize")
    @Expose
    val trayItemSubtitleFontSize : String?,

    @SerializedName("trayItemFontWeight")
    @Expose
    val trayItemFontWeight : String?,

    @SerializedName("buttonFontFamilyName")
    @Expose
    val buttonFontFamilyName : String?,

    @SerializedName("buttonFontSize")
    @Expose
    val buttonFontSize : String?,

    @SerializedName("buttonFontWeight")
    @Expose
    val buttonFontWeight : String?,

    @SerializedName("trayItemTitleFontFamilyName")
    @Expose
    val trayItemTitleFontFamilyName : String?,

    @SerializedName("trayItemTitleFontSize")
    @Expose
    val trayItemTitleFontSize : String?,

    @SerializedName("trayItemTitleFontWeight")
    @Expose
    val trayItemTitleFontWeight : String?,

    @SerializedName("teamFontFamilyName")
    @Expose
    val teamFontFamilyName : String?,

    @SerializedName("teamFontSize")
    @Expose
    val teamFontSize : String?,

    @SerializedName("teamFontWeight")
    @Expose
    val teamFontWeight : String?,
    ): Parcelable


@Keep @Parcelize
data class MetadataMap (

    @SerializedName("Quarter")
    @Expose
    val Quarter :String?,

    @SerializedName("final")
    @Expose
    val final :String?,

    @SerializedName("gamePreview")
    @Expose
    val gamePreview :String?,

    @SerializedName("gameStartsAt")
    @Expose
    val gameStartsAt :String?,

    @SerializedName("live")
    @Expose
    val live :String?,

    @SerializedName("period")
    @Expose
    val period :String?,

    @SerializedName("postGame")
    @Expose
    val postGame :String?,

    @SerializedName("poweredBy")
    @Expose
    val poweredBy :String?,

    @SerializedName("preGame")
    @Expose
    val preGame :String?,

    @SerializedName("readArticle")
    @Expose
    val readArticle :String?,

    @SerializedName("seeMoreTxt")
    @Expose
    val seeMoreTxt :String?,

    @SerializedName("watchNowInGameCenter")
    @Expose
    val watchNowInGameCenter :String?,
): Parcelable
