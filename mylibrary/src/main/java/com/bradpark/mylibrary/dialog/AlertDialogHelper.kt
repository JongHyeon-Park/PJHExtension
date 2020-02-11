package com.siwonschool.ui.dialog

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.bradpark.mylibrary.R
import com.bradpark.mylibrary.databinding.DialogAlertBinding


class AlertDialogHelper(context: Context) : BaseDialogHelper() {

    private val dialogViewBinding: DialogAlertBinding by lazy {
        DataBindingUtil.inflate<DialogAlertBinding>(LayoutInflater.from(context), R.layout.dialog_alert, null, false)
    }

    override val dialogView: View by lazy {
        dialogViewBinding.root
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    private var isOneButton: Boolean = false

    fun cancelClickListener(func: (() -> Unit)? = null) =
            with(dialogViewBinding.tvCancel) {
                setBtnClickListener(func)
            }

    fun confirmClickListener(func: (() -> Unit)? = null) =
            with(dialogViewBinding.tvConfirm) {
                setBtnClickListener(func)
            }

    fun initView(title: String, message: String) {
        dialogViewBinding.tvTitle.text = title
        dialogViewBinding.tvMsg.text = message
        if (isOneButton) {
            dialogViewBinding.tvCancel.visibility = View.GONE
        } else {
            dialogViewBinding.tvCancel.visibility = View.VISIBLE
        }
    }

    fun setOneButton(isOneBtn: Boolean) {
        isOneButton = isOneBtn
        if (isOneButton) {
            dialogViewBinding.tvCancel.visibility = View.GONE
        } else {
            dialogViewBinding.tvCancel.visibility = View.VISIBLE
        }
    }

    private fun View.setBtnClickListener(func: (() -> Unit)?) =
            setOnClickListener {
                func?.invoke()
                dialog?.dismiss()
            }
}
