package com.siwonschool.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window

class CustomDialog(context: Context, view: View): Dialog(context) {
    private var mListener: DialogClickListener? = null
    private var mIsClickConfirm: Boolean = false

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        setContentView(view)


        setOnDismissListener {
            if (!mIsClickConfirm) mListener?.onClose()
        }
    }

    interface DialogClickListener {
        fun onClose()
        fun onConfirm()
    }

    fun setClickListener(listener: DialogClickListener?) {
        mListener = listener
    }

    fun onClose() {
        dismiss()
    }

    fun onConfirm() {
        mIsClickConfirm = true
        dismiss()
        mListener?.onConfirm()
    }
}