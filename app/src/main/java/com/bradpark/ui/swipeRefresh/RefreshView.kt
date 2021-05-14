package com.bradpark.ui.swipeRefresh

interface RefreshView {
    fun stopRefreshing()
    fun isRefreshing(): Boolean
    fun startRefreshing(smooth: Boolean = false)
}