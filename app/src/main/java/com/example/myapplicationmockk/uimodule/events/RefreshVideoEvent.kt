package com.viewlift.uimodule.events

/**
 * Page event
 * @author Anand
 *
 * @constructor Create empty Page event
 */
sealed class RefreshVideoEvent {
    /**
     * Open details page
     *
     * @property uri
     * @constructor Create empty Open details page
     */
    data class RefreshVideo(val uri: String) : RefreshVideoEvent()
}
