package com.bradpark.extensions

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bradpark.pjhextension.R
import com.bradpark.utils.PjhUi

internal fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}

/**
 * View Margin Setting
 * @receiver View
 * @param left Float DP
 * @param top Float DP
 * @param right Float DP
 * @param bottom Float DP
 */
fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = PjhUi.convertDpToPixels(this@margin.context, this).toInt() }
        top?.run { topMargin = PjhUi.convertDpToPixels(this@margin.context, this).toInt() }
        right?.run { rightMargin = PjhUi.convertDpToPixels(this@margin.context, this).toInt() }
        bottom?.run { bottomMargin = PjhUi.convertDpToPixels(this@margin.context, this).toInt() }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

/**
 * 오른쪽에서 들어오는 애니메이션
 * @receiver View
 */
fun View.slideInRight() {
    visibility = View.VISIBLE
    val slideLeft: Animation = AnimationUtils.loadAnimation(this.context, R.anim.slide_in_right)
    startAnimation(slideLeft)
}

/**
 * 오른쪽에서 나가는 애니메이션
 * @receiver View
 */
fun View.slideOutRight() {
    visibility = View.GONE
    val slideLeft: Animation = AnimationUtils.loadAnimation(this.context, R.anim.slide_out_right)
    startAnimation(slideLeft)
}

/**
 * 왼쪽에서 들어오는 애니메이션
 * @receiver View
 */
fun View.slideInLeft() {
    val slideLeft: Animation = AnimationUtils.loadAnimation(this.context, R.anim.slide_in_left)
    startAnimation(slideLeft)
}

/**
 * 왼쪽으로 나가는 애니메이션
 * @receiver View
 */
fun View.slideOutLeft() {
    val slideLeft: Animation = AnimationUtils.loadAnimation(this.context, R.anim.slide_out_left)
    startAnimation(slideLeft)
}

/**
 * 아래로 나가는 애니메이션
 * @receiver View
 */
fun View.slideOutBottom() {
    val slideBottom: Animation = AnimationUtils.loadAnimation(this.context, R.anim.slide_bottom_out)
    startAnimation(slideBottom)
}