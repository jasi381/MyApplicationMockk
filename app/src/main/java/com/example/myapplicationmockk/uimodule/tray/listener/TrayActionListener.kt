package com.viewlift.uimodule.tray.listener

interface TrayActionListener {
    fun playFromTrayAction(contentId: String)
    fun seeMoreTrayAction(categoryParmaLink: String)
    fun clickItemTrayAction(contentId: String)
    fun shareFromTrayAction(permalink :String)
    /**
     * Following will be taken care or used later
     */
    /*
    fun addWatchlistTrayAction()
    fun removeWatchlistTrayAction()*/
}