package com.siwonschool.ui.listener

import android.view.View

interface IntroduceSortClickCallback {
    fun onSortClick(view: View, sortPosition: Int, sectionedPosition: Int)
}