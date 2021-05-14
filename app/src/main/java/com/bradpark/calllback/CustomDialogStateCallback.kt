package com.bradpark.calllback

import android.view.View
import com.bradpark.ui.dialog.CustomDialog

interface CustomDialogStateCallback {
    fun onResumeState(dialog: CustomDialog, customView: View?, buttonView: View?)
}