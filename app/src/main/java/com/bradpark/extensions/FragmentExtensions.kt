package com.bradpark.extensions

import androidx.fragment.app.Fragment

/**
 * 키보드 닫기
 * @receiver Fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}