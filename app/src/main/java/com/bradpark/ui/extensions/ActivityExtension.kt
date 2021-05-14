package com.bradpark.ui.extensions

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View

/**
 * 화면 사이즈 구하기
 *
 * @return
 */
fun Activity.displayMetrics(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

/**
 * 키보드 닫기
 * @receiver Activity
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * 키보드 닫기
 * @receiver Activity
 * @param view View
 */
fun Activity.hideViewKeyboard(view: View) {
    hideKeyboard(view)
}