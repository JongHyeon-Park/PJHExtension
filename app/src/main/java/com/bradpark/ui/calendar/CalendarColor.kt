package com.bradpark.ui.calendar

import android.os.Parcelable
import com.bradpark.pjhextension.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarColor (
    val selectedBackground: Int = R.drawable.radius_6_maincolor,
    val selectedTextColor: Int = R.color.colorWhite,
    val baseTextColor: Int = R.color.gray08,
    val todayTextColor: Int = R.color.blue02,
    val beforeDayTextColor: Int = R.color.gray05
): Parcelable