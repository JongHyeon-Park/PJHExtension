package com.bradpark.mylibrary.banner

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller

class BannerViewPager @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null) : androidx.viewpager.widget.ViewPager(context, attrs) {

    private var enableSwipe = true
    private var smoothScroll = true
    private var scrollHandler: Handler = Handler()
    private var autoScrollDelay = 3000L
    private var enableInfiniteSwipe = true

    private val autoScroll = Runnable {
        setCurrentItem(currentItem + 1, smoothScroll)
        startAutoScroll()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            this.enableSwipe && return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return this.enableSwipe && super.onTouchEvent(ev)
    }

    fun startAutoScroll() {
        stopAutoScroll()
        this.scrollHandler.postDelayed(autoScroll, autoScrollDelay)
    }

    fun stopAutoScroll() {
        this.scrollHandler.removeCallbacks(autoScroll)
    }

    internal fun setScrollDelay(ms: Int) {
        try {
            val viewpager = androidx.viewpager.widget.ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, DelayScroller(context, ms))
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalArgumentException) {
        } catch (e: IllegalAccessException) {
        }
    }

    internal fun enableSwipe(enable: Boolean) {
        this.enableSwipe = enable
    }

    internal fun enableInfiniteSwipe(enable: Boolean) {
        this.enableSwipe = enable
        if (enable) startAutoScroll() else stopAutoScroll()
    }

    internal fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
        enableInfiniteSwipe(enableInfiniteSwipe)
    }

    internal fun setSmoothScroll(smoothScroll: Boolean) {
        this.smoothScroll = smoothScroll
    }

    internal fun setDelay(delay: Long) {
        this.autoScrollDelay = delay
    }

    private inner class DelayScroller(context: Context, val durationScroll: Int = 250) : Scroller(context, DecelerateInterpolator()) {
        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            super.startScroll(startX, startY, dx, dy, durationScroll)
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, durationScroll)
        }
    }
}