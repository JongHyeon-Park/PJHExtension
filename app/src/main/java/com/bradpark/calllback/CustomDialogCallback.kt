package com.bradpark.calllback

import android.os.Parcelable
import android.view.View
import java.io.Serializable

interface CustomDialogCallback {
    fun onFirstClick()
    fun onSecondClick()
    fun onThirdClick()
}