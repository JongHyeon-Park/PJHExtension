package com.bradpark.ui.pdfviewer.interfaces


interface OnPageChangedListener {
    fun onPageChanged(page : Int, total : Int)
}