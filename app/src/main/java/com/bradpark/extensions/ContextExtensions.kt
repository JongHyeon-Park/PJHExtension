package com.bradpark.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File

/**
 * 키보드 닫기
 * @receiver Context
 * @param view View
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * 기기 남은 용량 가져오기
 * @receiver Context
 * @return Long
 */
fun Context.availableSpace() = File(filesDir.absoluteFile.toString()).usableSpace