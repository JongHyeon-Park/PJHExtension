package com.siwonschool.ui.Utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bradpark.mylibrary.R
import com.siwonschool.ui.dialog.CustomDialog
import com.siwonschool.ui.listener.SingleChoiceDialogListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UiUtils {

    companion object {

        @JvmStatic
        fun showSnackBar(rootView: View, msg: String) {
            Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show()
        }

        @JvmStatic
        fun convertPixelsToDp(context: Context, px: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return px / (metrics.densityDpi / 160f)
        }

        @JvmStatic
        fun convertDpToPixels(context: Context, dp: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi / 160f)
        }

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
                        if (isTextWhite) {
                            statusBarColor = color
                        } else {
                            statusBarColor = Color.parseColor("#2c2c2c")
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

        @JvmStatic
        fun convertTimeText(context: Context, time: String): ArrayList<String> {
            val timeStrList: ArrayList<String> = ArrayList()
            try {
                time.split(":").run {
                    var minute: String = this[1]
                    if (minute.length < 2) {
                        minute = "0${minute}"
                    }

                    val hour = this[0].toInt()
                    if (hour == 0 || hour == 24) {
                        timeStrList.add(context.getString(R.string.time_am))
                        timeStrList.add("00:${minute}")
                    } else if (hour < 12) {
                        if (hour < 10) {
                            timeStrList.add(context.getString(R.string.time_am))
                            timeStrList.add("0${hour}:${minute}")
                        } else {
                            timeStrList.add(context.getString(R.string.time_am))
                            timeStrList.add("${hour}:${minute}")
                        }
                    } else {
                        if (hour == 12) {
                            timeStrList.add(context.getString(R.string.time_pm))
                            timeStrList.add("${hour}:${minute}")
                        } else {
                            if (hour - 12 < 10) {
                                timeStrList.add(context.getString(R.string.time_pm))
                                timeStrList.add("0${hour - 12}:${minute}")
                            } else {
                                timeStrList.add(context.getString(R.string.time_pm))
                                timeStrList.add("${hour - 12}:${minute}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {}


            return timeStrList
        }

        @JvmStatic
        fun converWeekText(context: Context, weekMap: Map<Int, Boolean>?, color: Int): SpannableStringBuilder {
            var weekStr = ""
            var allWeekStr = ""
            context.apply {
                allWeekStr = getString(R.string.week_mon) + "    " +
                        getString(R.string.week_tue) + "    " +
                        getString(R.string.week_wed) + "    " +
                        getString(R.string.week_thu) + "    " +
                        getString(R.string.week_fri) + "    " +
                        getString(R.string.week_sat) + "    " +
                        getString(R.string.week_sun)
            }

            val spannableString = getSpannableStringBuilder(allWeekStr, "", color, true)

            weekMap?.forEach {
                when (it.key) {
                    0 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_sun), color, true)
                    }
                    1 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_mon), color, true)
                    }
                    2 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_tue), color, true)
                    }
                    3 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_wed), color, true)
                    }
                    4 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_thu), color, true)
                    }
                    5 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_fri), color, true)
                    }
                    6 -> if (it.value) {
                        addSpannableStringBuilder(spannableString, allWeekStr, context.getString(R.string.week_sat), color, true)
                    }
                }

            }

            return spannableString
        }

        @JvmStatic
        fun converWeekText(context: Context, week: String): String {
            var weekStr = week
            weekStr = weekStr.replace("0", context.getString(R.string.week_sun))
            weekStr = weekStr.replace("1", context.getString(R.string.week_mon))
            weekStr = weekStr.replace("2", context.getString(R.string.week_tue))
            weekStr = weekStr.replace("3", context.getString(R.string.week_wed))
            weekStr = weekStr.replace("4", context.getString(R.string.week_thu))
            weekStr = weekStr.replace("5", context.getString(R.string.week_fri))
            weekStr = weekStr.replace("6", context.getString(R.string.week_sat))

            return weekStr
        }

        @JvmStatic
        fun showConfirmDialog(context: Context, title: String, message: String, btn: String, listener: DialogInterface.OnClickListener) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(btn, listener)
                show()
            }
        }

        @JvmStatic
        fun showConfirmDialog(context: Context, title: String, message: String, btn: String,
                              listener: DialogInterface.OnClickListener, dismissListener: DialogInterface.OnDismissListener) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(btn, listener)
                setOnDismissListener(dismissListener)
                show()
            }
        }

        @JvmStatic
        fun showAlertDialog(context: Context, title: String, message: String, positiveBtn: String,
                            negativeBtn: String, listener: DialogInterface.OnClickListener) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveBtn, listener)
                setNegativeButton(negativeBtn) { dialog, _ -> dialog.dismiss() }
                show()
            }
        }

        @JvmStatic
        fun showAlertDialog(context: Context, title: String, message: String, positiveBtn: String,
                            negativeBtn: String, positiveListener: DialogInterface.OnClickListener?,
                            negativeListener: DialogInterface.OnClickListener?,
                            dismissListener: DialogInterface.OnDismissListener) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveBtn, positiveListener)
                setNegativeButton(negativeBtn, negativeListener)
                setOnDismissListener(dismissListener)
                show()
            }
        }

        @JvmStatic
        fun showConfirmDialogFix(context: Context, title: String, message: String, btn: String,
                              listener: DialogInterface.OnClickListener, dismissListener: DialogInterface.OnDismissListener) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(btn, listener)
                setOnDismissListener(dismissListener)
                setCancelable(false)
                show()
            }
        }

        @JvmStatic
        fun showAlertDialogFix(context: Context, title: String, message: String, positiveBtn: String,
                               negativeBtn: String, positiveListener: DialogInterface.OnClickListener?,
                               negativeListener: DialogInterface.OnClickListener?,
                               dismissListener: DialogInterface.OnDismissListener) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveBtn, positiveListener)
                setNegativeButton(negativeBtn, negativeListener)
                setOnDismissListener(dismissListener)
                setCancelable(false)
                show()
            }
        }

        @JvmStatic
        fun showCustomDialog(context: Context, view: View, listener: CustomDialog.DialogClickListener?): CustomDialog {
            return CustomDialog(context, view).apply {
                setClickListener(listener)
                show()
            }
        }

        @JvmStatic
        fun showSingleChoiceDialog(context: Context, title: String, items: Array<String>, positiveBtn: String,
                                   negativeBtn: String, listener: SingleChoiceDialogListener, selectedIndex: Int) {

            var itemIndex = selectedIndex
            val dialogBuilder = AlertDialog.Builder(context).apply {
                setTitle(title)
                setSingleChoiceItems(items, selectedIndex) { _, which -> itemIndex = which }
                setPositiveButton(positiveBtn) { dialog, _ ->
                    dialog.dismiss()
                    listener.onConfirm(itemIndex)
                }
                setNegativeButton(negativeBtn) { dialog, _ -> dialog.dismiss() }
            }

            val dialog = dialogBuilder.create()
            dialog.show()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            val dialogWindowHeight = (UiUtils.convertDpToPixels(context, 400f)).toInt()
            layoutParams.height = dialogWindowHeight
            dialog.window?.attributes = layoutParams
        }

        @JvmStatic
        fun showDropDownMenu(context: Context, view: View, menuRes: Int, listener: PopupMenu.OnMenuItemClickListener) {
            PopupMenu(context, view).run {
                menuInflater.inflate(menuRes, menu)
                setOnMenuItemClickListener(listener)
                show()
            }
        }

        @JvmStatic
        fun showDropDownMenu(context: Context, view: View, menuList: List<String>, listener: PopupMenu.OnMenuItemClickListener) {
            PopupMenu(context, view).run {
                menuList.forEachIndexed { index, s ->
                    menu.add(0, index, 0, s)
                }
                setOnMenuItemClickListener(listener)
                show()
            }
        }

        @JvmStatic
        fun showDropDownMenu(context: Context, view: View, menuList: List<String>, menuList2: List<Int>, listener: PopupMenu.OnMenuItemClickListener) {
            PopupMenu(context, view).run {
                menuList.forEachIndexed { index, s ->
                    menu.add(0, menuList2[index], 0, s)
                }
                setOnMenuItemClickListener(listener)
                show()
            }
        }

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

        @JvmStatic
        fun isNewDate(date: String, interval: Int): Boolean {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val checkCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.MILLISECOND, 0)
            }

            try {
                if (date.contains("-")) {
                    val dateArr = date.split("-")
                    val year = dateArr[0].toInt()
                    val month = dateArr[1].toInt()
                    val day = dateArr[2].toInt()

                    checkCal.set(year, month - 1, day, 0, 0, 0)
                }

                val diffSec = (cal.timeInMillis - checkCal.timeInMillis) / 1000
                val diffDay = diffSec / (60 * 60 * 24)

                if (interval > diffDay) {
                    return true
                }

            } catch (e: Exception) {}

            return false
        }

        @JvmStatic
        fun isExpireDate(date: String): Boolean {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val checkCal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.MILLISECOND, 0)
            }

            try {
                if (date.contains("-")) {
                    val dateArr = date.split("-")
                    val year = dateArr[0].toInt()
                    val month = dateArr[1].toInt()
                    val day = dateArr[2].toInt()

                    checkCal.set(year, month - 1, day, 0, 0, 0)
                }

                if (cal.timeInMillis > checkCal.timeInMillis) {
                    return true
                }

            } catch (e: Exception) {}

            return false
        }

        @JvmStatic
        fun getDateFormatString(date: String, addDay: Int): String {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.MILLISECOND, 0)
            }

            try {
                if (date.contains("-")) {
                    val dateArr = date.split("-")
                    val year = dateArr[0].toInt()
                    val month = dateArr[1].toInt()
                    val day = dateArr[2].toInt()

                    cal.set(year, month - 1, day, 0, 0, 0)
                    cal.add(Calendar.DATE, addDay)

                    val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
                    return simpleDateFormat.format(cal.timeInMillis)
                }

            } catch (e: Exception) {}

            return ""
        }

        @JvmStatic
        fun getTimeToString(time: Long, pattern: String): String {
            val cal = Calendar.getInstance().apply {
                timeInMillis = time
            }

            try {
                val simpleDateFormat = SimpleDateFormat(pattern)
                return simpleDateFormat.format(cal.timeInMillis)

            } catch (e: Exception) {}

            return ""
        }

        @JvmStatic
        fun getCurrentDateFormatString(pattern: String, suffix: String): String {
            val cal = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat(pattern)
            return "${simpleDateFormat.format(cal.timeInMillis)} ${suffix}"
        }
    }

}

