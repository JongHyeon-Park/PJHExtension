package com.bradpark.ui.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.bradpark.pjhextension.R
import com.bradpark.utils.PjhUi

class PjhToolbar {
    companion object {
        private lateinit var toolbar: Toolbar
        private var style: CubeActionBarStyle = CubeActionBarStyle.Default
        /**
         * 초기 Toolbar 값 세팅 toolbar module 필수
         *
         * @param activity
         */
        @JvmStatic
        fun setCustomToolbar(activity: Activity, id: Int) {
            toolbar = activity.findViewById<Toolbar>(id)
        }

        /**
         * Toolbar 반환
         *
         * @return
         */
        @JvmStatic
        fun getCustomToolbar(): Toolbar {
            return toolbar
        }

        /**
         * Toolbar Color 설정
         *
         * @param activity
         * @param color
         */
        fun setToolbarColor(activity: Activity, color: Int) {
            val toolBarId = getResourcesId(activity, "tool_bar", activity.packageName)
            toolbar.findViewById<Toolbar>(toolBarId)?.run {
                setBackgroundColor(color)
            }
        }

        /**
         * back 아이콘 변경
         *
         * @param activity
         * @param icon
         */
        @JvmStatic
        fun setBackIcon(activity: Activity, icon: Int) {
            val backId = getResourcesId(activity, "iv_toolbar_back", activity.packageName)
            toolbar.findViewById<ImageView>(backId)?.run {
                setImageResource(icon)
            }
        }

        /**
         * back onClick listener
         *
         * @param activity
         * @param onClick
         */
        @JvmStatic
        fun setBackClickListener(activity: Activity, onClick: View.OnClickListener?) {
            val backId = getResourcesId(activity, "iv_toolbar_back", activity.packageName)
            toolbar.findViewById<ImageView>(backId)?.run {
                setOnClickListener(onClick)
            }
        }

        /**
         * 임시 back 오른쪽 아이콘 변경
         *
         * @param activity
         * @param icon
         */
        @JvmStatic
        fun setTempIcon(activity: Activity, icon: Int) {
            val tempId = getResourcesId(activity, "iv_toolbar_back_after", activity.packageName)
            toolbar.findViewById<ImageView>(tempId)?.run {
                setImageResource(icon)
            }
        }

        /**
         * 임시 back onClick listener
         *
         * @param activity
         * @param onClick
         */
        @JvmStatic
        fun setTempClickListener(activity: Activity, onClick: View.OnClickListener?) {
            val tempId = getResourcesId(activity, "iv_toolbar_back_after", activity.packageName)
            toolbar.findViewById<ImageView>(tempId)?.run {
                setOnClickListener(onClick)
            }
        }

        /**
         * menu 아이콘 변경
         *
         * @param activity
         * @param icon
         */
        @JvmStatic
        fun setMenuIcon(activity: Activity, icon: Int) {
            val menuId = getResourcesId(activity, "iv_toolbar_menu", activity.packageName)
            toolbar.findViewById<ImageView>(menuId)?.run {
                setImageResource(icon)
            }
        }

        /**
         * menu onClick listener
         *
         * @param activity
         * @param onClick
         */
        @JvmStatic
        fun setMenuClickListener(activity: Activity, onClick: View.OnClickListener?) {
            val menuId = getResourcesId(activity, "iv_toolbar_menu", activity.packageName)
            toolbar.findViewById<ImageView>(menuId)?.run {
                setOnClickListener(onClick)
            }
        }

        /**
         * search 아이콘 변경
         *
         * @param activity
         * @param icon
         */
        @JvmStatic
        fun setSearchIcon(activity: Activity, icon: Int) {
            val searchId = getResourcesId(activity, "iv_toolbar_search", activity.packageName)
            toolbar.findViewById<ImageView>(searchId)?.run {
                setImageResource(icon)
            }
        }

        /**
         * search onClick listener
         *
         * @param activity
         * @param onClick
         */
        @JvmStatic
        fun setSearchClickListener(activity: Activity, onClick: View.OnClickListener?) {
            val searchId = getResourcesId(activity, "iv_toolbar_search", activity.packageName)
            toolbar.findViewById<ImageView>(searchId)?.run {
                setOnClickListener(onClick)
            }
        }

        /**
         * setActionBarStyle setting
         *
         * @param activity
         * @param mode
         */
        @JvmStatic
        fun setActionBarStyle(activity: Activity, mode: CubeActionBarStyle) {
            this.style = mode
            val toolbarId = PjhToolbar.getResourcesId(activity, "cl_toolbar", activity.packageName)
            toolbar.findViewById<ConstraintLayout>(toolbarId)?.let { constraint ->
                val titleId =
                    PjhToolbar.getResourcesId(activity, "tv_toolbar_title", activity.packageName)
                val title = toolbar.findViewById<TextView>(titleId)
                val param = when (mode) {
                    CubeActionBarStyle.Large -> Toolbar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PjhUi.convertDpToPixels(activity, 56f).toInt())
                    CubeActionBarStyle.Default -> Toolbar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, PjhUi.convertDpToPixels(activity, 49f).toInt())
                }
                when (mode) {
                    CubeActionBarStyle.Default -> {
                        if (Build.VERSION.SDK_INT < 23) {
                            title.setTextAppearance(activity, R.style.SubTitle)
                        } else {
                            title.setTextAppearance(R.style.SubTitle)
                        }
                    }
                    CubeActionBarStyle.Large -> {
                        if (Build.VERSION.SDK_INT < 23) {
                            title.setTextAppearance(activity,R.style.H3)
                        } else {
                            title.setTextAppearance(R.style.H3)
                        }
                    }
                }
                val margin = PjhUi.convertDpToPixels(activity, 16f).toInt()
                val params = title.layoutParams as ViewGroup.MarginLayoutParams
                params.marginStart = margin
                params.marginEnd = margin
                title.layoutParams = params
                constraint.layoutParams = param
                when (mode) {
                    CubeActionBarStyle.Default -> {
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(constraint)
                        constraintSet.connect(R.id.tv_toolbar_title, ConstraintSet.START, R.id.iv_toolbar_back_after, ConstraintSet.END)
                        constraintSet.connect(R.id.tv_toolbar_title, ConstraintSet.END, R.id.iv_toolbar_search, ConstraintSet.START)
                        constraintSet.applyTo(constraint)
                    }
                    CubeActionBarStyle.Large -> {
                        title.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(constraint)
                        constraintSet.connect(R.id.tv_toolbar_title, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                        constraintSet.applyTo(constraint)
                    }
                }
            }
        }

        /**
         * Type Default 일때 Title Text 가 TextView 보다 큰 경우 ConstraintSet 변경 및 말풍선 클릭 제공
         * @param activity Activity
         */
        @JvmStatic
        fun checkActionBarDefaultConstraint(activity: Activity, event: ToolBarTitle? = null, bubbleClickMode: BubbleClickMode = BubbleClickMode.None, isBackTempGone: Boolean = false) {
            if (this.style == CubeActionBarStyle.Default) {
                val titleId =
                    getResourcesId(activity, "tv_toolbar_title", activity.packageName)
                val title = toolbar.findViewById<TextView>(titleId)
                val backTempId = getResourcesId(
                    activity,
                    "iv_toolbar_back_after",
                    activity.packageName
                )

                title.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (isTooLarge(title, title.text.toString())) {
                            title.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                            title.ellipsize = TextUtils.TruncateAt.END
                            if (bubbleClickMode == BubbleClickMode.End) {
                                getEllipsis(title, object : ToolBarTitleAlign {           //  Ellipsis 상태에서만 노출
                                    override fun getData(check: Boolean) {
                                        if (check) {
                                            title.setOnClickListener { event?.onClick() }
                                        }
                                    }
                                })
                            }
                            toolbar.findViewById<ImageView>(backTempId)?.run {
                                visibility = View.GONE
                            }
                        } else {
                            toolbar.findViewById<ImageView>(backTempId)?.run {
                                visibility = if (isBackTempGone) {
                                    View.GONE
                                } else {
                                    View.VISIBLE
                                }
                            }
                        }
                        title.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
                if (bubbleClickMode == BubbleClickMode.All) {
                    title.setOnClickListener { event?.onClick() }
                }
            }
        }

        /**
         * Title 문구 변경
         *
         * @param activity
         * @param title
         */
        @JvmStatic
        fun setTitleText(activity: Activity, title: String) {
            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)
            toolbar.findViewById<TextView>(titleId)?.run {
                text = title
            }
        }

        /**
         * title onClick listener
         * @param activity Activity
         * @param onClick OnClickListener?
         */
        @JvmStatic
        fun setTitleClickListener(activity: Activity, onClick: View.OnClickListener?) {
            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)
            toolbar.findViewById<TextView>(titleId)?.run {
                setOnClickListener(onClick)
            }
        }

        /**
         * Title 사이즈 변경
         *
         * @param activity
         * @param size
         */
        @JvmStatic
        fun setTitleSize(activity: Activity, size: Float) {
            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)
            toolbar.findViewById<TextView>(titleId)?.run {
                textSize = size
            }
        }

        /**
         * Title color 변경
         *
         * @param activity
         * @param color
         */
        @JvmStatic
        fun setTitleColor(activity: Activity, color: Int) {
            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)
            toolbar.findViewById<TextView>(titleId)?.run {
                setTextColor(color)
            }
        }

        /**
         * Title text align 변경
         *
         * @param activity
         * @param textAlign
         */
        @JvmStatic
        fun setTitleTextAlign(activity: Activity, textAlign: Int) {
            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)
            toolbar.findViewById<TextView>(titleId)?.run {
                textAlignment = textAlign
            }
        }

        /**
         * Select 문구 변경
         *
         * @param activity
         * @param message
         */
        @JvmStatic
        fun setSelectText(activity: Activity, message: String) {
            val selectId = getResourcesId(activity, "tv_toolbar_select", activity.packageName)
            toolbar.findViewById<TextView>(selectId)?.run {
                text = message
            }
        }

        /**
         * Select color 변경
         *
         * @param activity
         * @param color
         */
        @JvmStatic
        fun setSelectColor(activity: Activity, color: Int) {
            val selectId = getResourcesId(activity, "tv_toolbar_select", activity.packageName)
            toolbar.findViewById<TextView>(selectId)?.run {
                setTextColor(color)
            }
        }

        /**
         * Select 사이즈 변경
         *
         * @param activity
         * @param size
         */
        @JvmStatic
        fun setSelectSize(activity: Activity, size: Float) {
            val selectId = getResourcesId(activity, "tv_toolbar_select", activity.packageName)
            toolbar.findViewById<TextView>(selectId)?.run {
                textSize = size
            }
        }

        /**
         * Select onClick listener
         *
         * @param activity
         * @param onClick
         */
        @JvmStatic
        fun setSelectClickListener(activity: Activity, onClick: View.OnClickListener?) {
            val selectId = getResourcesId(activity, "tv_toolbar_select", activity.packageName)
            toolbar.findViewById<TextView>(selectId)?.run {
                setOnClickListener(onClick)
            }
        }

        /**
         * Toolbar 색상 변경
         *
         * @param color
         */
        @JvmStatic
        fun setToolbarBackgroundColor(color: Int) {
            toolbar.setBackgroundColor(color)
        }

        /**
         * Toolbar Visibility 설정
         *
         * @param visibility
         */
        @JvmStatic
        fun setToolBarVisibility(visibility: Int = View.GONE) {
            toolbar.visibility = visibility
        }

        /**
         * Toolbar Visibility 설정
         *
         * @param activity
         * @param backVisibility
         * @param menuVisibility
         * @param searchVisibility
         * @param titleVisibility
         */
        @JvmStatic
        fun setToolBarItemVisibility(activity: Activity, backVisibility: Int = View.GONE, tempVisibility: Int = View.GONE, menuVisibility: Int = View.GONE, searchVisibility: Int = View.GONE, titleVisibility: Int = View.GONE, selectVisibility: Int = View.GONE, animation: Boolean = false) {
            fun executeTextAnimation(textView: TextView) {
                textView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in))
            }

            fun executeImageAnimation(imageView: ImageView) {
                imageView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in))
            }
            val backId = getResourcesId(activity, "iv_toolbar_back", activity.packageName)
            toolbar.findViewById<ImageView>(backId)?.run {
                visibility = backVisibility
                if (animation) {
                    executeImageAnimation(this)
                }
            }

            val menuId = getResourcesId(activity, "iv_toolbar_menu", activity.packageName)
            toolbar.findViewById<ImageView>(menuId)?.run {
                visibility = menuVisibility
                if (animation) {
                    executeImageAnimation(this)
                }
            }

            val searchId = getResourcesId(activity, "iv_toolbar_search", activity.packageName)
            toolbar.findViewById<ImageView>(searchId)?.run {
                visibility = searchVisibility
                if (animation) {
                    executeImageAnimation(this)
                }
            }

            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)
            toolbar.findViewById<TextView>(titleId)?.run {
                visibility = titleVisibility
                if (animation) {
                    executeTextAnimation(this)
                }
            }

            val selectId = getResourcesId(activity, "tv_toolbar_select", activity.packageName)
            toolbar.findViewById<TextView>(selectId)?.run {
                visibility = selectVisibility
                if (animation) {
                    executeTextAnimation(this)
                }
            }

            val backTempId = getResourcesId(activity, "iv_toolbar_back_after", activity.packageName)
            toolbar.findViewById<ImageView>(backTempId)?.run {
                visibility = tempVisibility
                if (animation) {
                    executeImageAnimation(this)
                }
            }
        }

        /**
         * Id 찾기
         *
         * @param context
         * @param id
         * @param packageName
         * @return
         */
        @JvmStatic
        fun getResourcesId(context: Context, id: String, packageName: String): Int {
            return context.resources.getIdentifier(id,"id",packageName)
        }
        /**
         * LongText click Title 사용법
         * xml 아래 선언 (id 동일)
         *
         * <include
            android:id="@+id/custom_speech_bubble"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            layout="@layout/speech_bubble"
            android:visibility="gone"/>
         */

        /**
         * get speechBubble visibility
         *
         * @param activity
         */
        @JvmStatic
        fun isSpeechBubbleVisibility(activity: Activity): Int {
            val constraintId = PjhToolbar.getResourcesId(activity, "custom_speech_bubble", activity.packageName)
            val constraint = activity.findViewById<ConstraintLayout>(constraintId)
            return  constraint.visibility
        }

        /**
         * set speechBubble visibility
         *
         * @param activity
         */
        @JvmStatic
        fun setSpeechBubbleVisible(activity: Activity, value: Int) {
            val constraintId = PjhToolbar.getResourcesId(activity, "custom_speech_bubble", activity.packageName)
            val constraint = activity.findViewById<ConstraintLayout>(constraintId)
            constraint.visibility = value
        }

        /**
         * 말풍선 text Color 처리 와 높이 처리 하는 부분
         *
         * @param activity
         * @param solidColor
         * @param textColor
         */
        @JvmStatic
        fun setSpeechBubbleColor(activity: Activity, bubbleDrawableId: Int = R.drawable.triangle, solidColor: Int = R.color.colorPrimary, textColor: Int = R.color.colorWhite, margin: Int = 0) {
            val constraintId = getResourcesId(activity, "custom_speech_bubble", activity.packageName)
            val constraint = activity.findViewById<ConstraintLayout>(constraintId)

            val titleId = getResourcesId(activity, "tv_toolbar_title", activity.packageName)

            val toolbarTitle = toolbar.findViewById<TextView>(titleId)

            val imageId = getResourcesId(activity, "iv_speech_bubble", activity.packageName)
            val subConstraintId = getResourcesId(activity, "cl_speech_bubble", activity.packageName)
            val textId = getResourcesId(activity, "tv_speech_bubble", activity.packageName)
            val subConstraintParentId = getResourcesId(activity, "cl_speech_bubble_parent", activity.packageName)

            val image = constraint.findViewById<ImageView>(imageId)
            val subConstraint = constraint.findViewById<ConstraintLayout>(subConstraintId)
            val text = constraint.findViewById<TextView>(textId)
            val subConstraintParent = constraint.findViewById<ConstraintLayout>(subConstraintParentId)

            subConstraintParent.visibility = View.VISIBLE
            image.setBackgroundResource(bubbleDrawableId)

            val layout = image.layoutParams as ConstraintLayout.LayoutParams
            layout.topMargin =  toolbarTitle.bottom + margin
            subConstraint.setBackgroundResource(R.drawable.radius_6)
            val drawable = subConstraint.background as GradientDrawable
            drawable.setColor(ContextCompat.getColor(activity, solidColor))

            subConstraint.setOnClickListener {
                constraint.visibility = View.GONE
            }
            text.setTextColor(ContextCompat.getColor(activity, textColor))


        }

        /**
         * speechBubble 문구 변경
         *
         * @param activity
         * @param message
         */
        @JvmStatic
        fun setSpeechBubbleText(activity: Activity, message: String) {
            val constraintId = PjhToolbar.getResourcesId(activity, "custom_speech_bubble", activity.packageName)
            val constraint = activity.findViewById<ConstraintLayout>(constraintId)
            val titleId = getResourcesId(activity, "tv_speech_bubble", activity.packageName)
            constraint.findViewById<TextView>(titleId)?.run {
                text = message
            }
        }

        /**
         * speechBubble size 변경
         *
         * @param activity
         * @param size
         */
        @JvmStatic
        fun setSpeechBubbleTextSize(activity: Activity, size: Float) {
            val constraintId = PjhToolbar.getResourcesId(activity, "custom_speech_bubble", activity.packageName)
            val constraint = activity.findViewById<ConstraintLayout>(constraintId)
            val titleId = getResourcesId(activity, "tv_speech_bubble", activity.packageName)
            constraint.findViewById<TextView>(titleId)?.run {
                textSize = size
            }
        }

        /**
         * ellipsis ViewTreeObserver
         *
         * @param textView
         * @return
         */
        @JvmStatic
        fun getEllipsis(textView: TextView, align: ToolBarTitleAlign) {
            textView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (textView.layout.getEllipsisCount(textView.lineCount -1) > 0) {
                        align.getData(true)
                    } else {
                        align.getData(false)
                    }
                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

        /**
         * TextView 보다 Text 가 넘치는 상태 체크
         * @param text TextView
         * @param newText String
         * @return Boolean
         */
        fun isTooLarge(text: TextView, newText: String): Boolean {
            val textWidth = text.paint.measureText(newText)
            return textWidth >= text.measuredWidth
        }

        /**
         * ellipsis 상태 체크
         *
         * @param textView
         * @return
         */
        @JvmStatic
        fun isEllipsis(textView: TextView): Boolean {
            if (textView.layout != null) {
                return textView.layout.getEllipsisCount(textView.lineCount -1) > 0
            }
            return false
        }

        /**
         * Get Current ActionBar Style
         *
         */
        @JvmStatic
        fun getActionBarStyle() = style

    }

    interface ToolBarTitle {
        fun onClick()
    }

    interface ToolBarTitleAlign {
        fun getData(check: Boolean)
    }

    enum class CubeActionBarStyle {
        Large , Default;
    }

    enum class BubbleClickMode {
        All, End, None;
    }
}