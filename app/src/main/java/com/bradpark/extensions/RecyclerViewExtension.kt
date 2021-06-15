package com.bradpark.extensions

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView Scroll position 최 상단
 * @receiver RecyclerView
 * @param position Int
 * @param snapMode Int
 */
fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

/**
 * RecyclerView Animation Off
 * @receiver RecyclerView
 */
fun RecyclerView.offAnimation() {
    this.itemAnimator = null
}