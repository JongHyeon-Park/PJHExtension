package com.bradpark.calllback

import android.view.View
import com.bradpark.ui.dialog.CustomSpannableDialog

interface CustomSpannableDialogStateCallback {
    fun onResumeState(dialog: CustomSpannableDialog, customView: View?, buttonView: View?)
}