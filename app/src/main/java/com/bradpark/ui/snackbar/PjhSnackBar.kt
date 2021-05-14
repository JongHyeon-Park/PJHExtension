package com.bradpark.ui.snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.bradpark.ui.extensions.findSuitableParent
import com.bradpark.utils.PjhUi
import com.google.android.material.snackbar.BaseTransientBottomBar
import io.crscube.cubekit.listener.OnSwipeTouchListener

/**
 *  큐브 스낵바
 *
 * @constructor
 *
 * @param parent
 * @param content
 * @param durationValue
 */
class PjhSnackBar(
    parent: ViewGroup,
    content: PjhSnackBarView,
    durationValue: Int = 5000
) : BaseTransientBottomBar<PjhSnackBar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
        duration = durationValue
        content.setOnTouchListener(object: OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
            }
            override fun onSwipeRight() {
            }
            override fun onSwipeTop() {
            }
            override fun onSwipeBottom() {
                dismiss()
            }
        })
    }

    companion object {

        /**
         * 큐브 스낵바 생성
         *
         * @param view 띄워질 뷰
         * @param mode 스낵바 타입
         * @param message 내용
         * @param iconId 아이콘 ResourceID
         * @param type 스낵바 위치 타입
         * @param navigationType 소프트웨어 키보드 여부
         * @return
         */
        fun make(view: View, mode: PjhConfig.PjhSnackBarType, message: String, iconId: Int, type: PjhConfig.PjhSnackPositionType = PjhConfig.PjhSnackPositionType.NORMAL, navigationType: PjhConfig.PjhSnackNavigationBarStatus = PjhConfig.PjhSnackNavigationBarStatus.No): PjhSnackBar {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_snackbar,
                parent,
                false
            ) as PjhSnackBarView
            customView.setData(mode, message, iconId)
            val params = customView.layoutParams as ViewGroup.MarginLayoutParams
            val value = when (type) {
                PjhConfig.PjhSnackPositionType.NORMAL -> {
                    PjhUi.convertDpToPixels(view.context, 8f)
                }
                PjhConfig.PjhSnackPositionType.NOTCH -> {
                    PjhUi.convertDpToPixels(view.context, 34f)
                }
                PjhConfig.PjhSnackPositionType.MAIN_BUTTON -> {
                    PjhUi.convertDpToPixels(view.context, 56f)
                }
                PjhConfig.PjhSnackPositionType.BOTTOM_NAVIGATION -> {
                    PjhUi.convertDpToPixels(view.context, 48f)
                }
            }
            var calcValue = value.toInt()
            if (navigationType == PjhConfig.PjhSnackNavigationBarStatus.Yes) {
                val resourceId: Int = view.context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    calcValue += view.context.resources.getDimensionPixelSize(resourceId)
                }
            }
            params.bottomMargin = calcValue
            // We create and return our Snackbar
            return PjhSnackBar(
                parent,
                customView
            )
        }

    }

}