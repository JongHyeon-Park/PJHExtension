package com.bradpark.mylibrary.Utils

import android.graphics.Color
import android.text.TextPaint
import android.text.style.UnderlineSpan

class ColorUnderlineSpan(underlineColor: Int = Color.BLACK): UnderlineSpan() {
    private var mUnderlineColor: Int = underlineColor

    override fun updateDrawState(ds: TextPaint?) {
        ds?.apply {
            super.updateDrawState(this)
            color = mUnderlineColor
        }

    }
}