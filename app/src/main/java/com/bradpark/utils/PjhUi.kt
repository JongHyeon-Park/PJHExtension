package com.bradpark.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bradpark.config.PjhConfig
import com.bradpark.ui.ColorUnderlineSpan
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class PjhUi {
    companion object {
        /**
         * SnackBar 생성
         *
         * @param rootView
         * @param msg
         */
        @JvmStatic
        fun showSnackBar(rootView: View, msg: String) {
            Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show()
        }

        /**
         * px 를 dp로 변환
         *
         * @param context
         * @param px
         * @return
         */
        @JvmStatic
        fun convertPixelsToDp(context: Context, px: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return px / (metrics.densityDpi / 160f)
        }

        /**
         * dp 를 px로 변환
         *
         * @param context
         * @param dp
         * @return
         */
        @JvmStatic
        fun convertDpToPixels(context: Context, dp: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi / 160f)
        }

        /**
         * 스크린 사이즈 가져오기
         *
         * @param activity
         * @return
         */
        @JvmStatic
        fun getScreenSize(activity: Activity): Point {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size
        }

        /**
         * 상단 상태바 색 변경
         *
         * @param activity
         * @param color
         * @param isTextWhite
         */
        @JvmStatic
        fun setStatusBarColor(activity: Activity, color: Int, isTextWhite: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.run {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        statusBarColor = color

                        if (isTextWhite) {
                            decorView.systemUiVisibility = 0
                        } else {
                            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        }
                    } else {
                        statusBarColor = if (isTextWhite) {
                            color
                        } else {
                            Color.parseColor("#2c2c2c")
                        }
                        decorView.systemUiVisibility = 0
                    }
                }
            }
        }

        /**
         * 상단 상태바 텍스트 색 변경 (롤리팝 이상 흰색, 검정만 설정 가능)
         *
         * @param activity
         * @param isTextWhite
         */
        @JvmStatic
        fun setStatusBarTextColor(activity: Activity, isTextWhite: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.window.run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (isTextWhite) {
                            decorView.systemUiVisibility = 0
                        } else {
                            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        }
                    }
                }
            }
        }

        /**
         * 특정 글자 색을 칠하기 위한 builder 반환
         *
         * @param word      원래 String
         * @param matchWord keyword
         * @param color     매칭되는 글자색상
         * @param isBold    bold 여부
         * @return
         */
        @JvmStatic
        fun getSpannableStringBuilder(word: String, matchWord: String, color: Int, isBold: Boolean): SpannableStringBuilder {
            val builder = SpannableStringBuilder(word)

            try {
                val startPos = word.indexOf(matchWord)

                if (startPos >= 0) {
                    val endPos = startPos + matchWord.length
                    builder.setSpan(ForegroundColorSpan(color), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    if (isBold) {
                        builder.setSpan(StyleSpan(Typeface.BOLD), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            } catch (e: Exception) {}

            return builder
        }

        /**
         * 특정 글자 색을 칠하기 위한 builder 반환
         *
         * @param word      원래 String
         * @param matchWord keyword
         * @param color     매칭되는 글자색상
         * @param isBold    bold 여부
         * @return
         */
        @JvmStatic
        fun getSpannableStringBuilderBold(word: String, matchWord: String, color: Int, isBold: Boolean,bold : Typeface?): SpannableStringBuilder {
            val builder = SpannableStringBuilder(word)

            try {
                val startPos = word.indexOf(matchWord)

                if (startPos >= 0) {
                    val endPos = startPos + matchWord.length
                    builder.setSpan(ForegroundColorSpan(color), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    if (isBold) {
                        builder.setSpan(StyleSpan(bold?.style?:0), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            } catch (e: Exception) {}

            return builder
        }

        @JvmStatic
        fun addSpannableStringBuilder(spanable: SpannableStringBuilder, word: String, matchWord: String, color: Int, isBold: Boolean) {
            try {
                val startPos = word.indexOf(matchWord)

                if (startPos >= 0) {
                    val endPos = startPos + matchWord.length
                    spanable.setSpan(ForegroundColorSpan(color), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    if (isBold) {
                        spanable.setSpan(StyleSpan(Typeface.BOLD), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            } catch (e: Exception) {}
        }

        /**
         * 특정글자에 밑줄처리를 위한 builder 반환
         *
         * @param word
         * @param matchWord
         * @param underlineWord
         * @param color
         * @return
         */
        @JvmStatic
        fun getUnderlineText(word: String, matchWord: String, underlineWord: String, color: Int)
                : SpannableStringBuilder {
            val builder = SpannableStringBuilder(word)

            val underlineStartPos = word.indexOf(underlineWord)
            if (underlineStartPos >= 0) {
                val underlineEndPos = underlineStartPos + underlineWord.length
                builder.setSpan(ColorUnderlineSpan(), underlineStartPos, underlineEndPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            val matchStartPos = word.indexOf(matchWord)
            if (matchStartPos >= 0) {
                val matchEndPos = matchStartPos + matchWord.length
                builder.setSpan(ForegroundColorSpan(color), matchStartPos, matchEndPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            return builder
        }

        /**
         * Tab Layout Margin 설정
         *
         * @param tabLayout
         * @param marginOffset
         */
        @JvmStatic
        fun reduceMarginsInTabs(tabLayout: TabLayout, marginOffset: Int) {
            val tabStrip = tabLayout.getChildAt(0)
            if (tabStrip is ViewGroup) {
                for (i in 0 until tabStrip.childCount) {
                    val tabView = tabStrip.getChildAt(i)
                    if (tabView.layoutParams is ViewGroup.MarginLayoutParams) {
                        (tabView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = marginOffset
                        (tabView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = marginOffset
                    }
                }

                tabLayout.requestLayout()
            }

        }

        /**
         * Lottie 값 세팅
         *
         * @param context
         * @param frameLayout
         * @param jsonFile
         * @param width
         * @param height
         */
        @JvmStatic
        fun setLottieData(context: Context, frameLayout: FrameLayout, jsonFile: Int? = null, width: Int, height: Int) {
            val lottie = LottieAnimationView(context)
            jsonFile?.let { lottie.setAnimation(it)}?: run { lottie.setAnimation("defaultloading.json") }
            lottie.playAnimation()
            lottie.repeatCount = LottieDrawable.INFINITE
            frameLayout.addView(lottie)
            lottie.layoutParams.width = width
            lottie.layoutParams.height = height
            val params = FrameLayout.LayoutParams(width,height)
            params.gravity = Gravity.CENTER
            lottie.layoutParams = params
        }

        /**
         * dialog 팝업 버튼색 변경
         *
         * @param dialog
         */
        @JvmStatic
        fun applyPopupButtonStyle(dialog: Dialog) {
            dialog.setOnShowListener {
                dialog.findViewById<TextView>(android.R.id.title)?.apply {
//                     typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                }
//

                dialog.findViewById<TextView>(android.R.id.message)?.apply {
//                    typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                }

                dialog.findViewById<Button>(android.R.id.button1)?.apply {              // 확인 버튼
//                    typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                    setTextColor(PjhConfig.mainColor)
                }

                dialog.findViewById<Button>(android.R.id.button2)?.apply {              // 취소 버튼
//                    typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                    setTextColor(PjhConfig.mainColor)
                }


            }
        }
    }
}