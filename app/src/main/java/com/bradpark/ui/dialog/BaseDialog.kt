package com.bradpark.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.bradpark.pjhextension.R
import com.bradpark.utils.PjhUi
import io.crscube.cubekit.listener.SingleChoiceDialogListener

class BaseDialog {
    companion object {
        /**
         * 버튼이 하나인 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param btn
         * @param listener
         * @param isCancel
         */
        @JvmStatic
        fun showConfirmDialog(context: Context, title: String, message: String, btn: String, listener: DialogInterface.OnClickListener
                              , isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(btn, listener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * 버튼이 하이나고 OnDismissListener 가 존재하는 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param btn
         * @param listener
         * @param dismissListener
         * @param isCancel
         */
        @JvmStatic
        fun showConfirmDialog(context: Context, title: String, message: String, btn: String,
                              listener: DialogInterface.OnClickListener, dismissListener: DialogInterface.OnDismissListener
                              , isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(btn, listener)
                setOnDismissListener(dismissListener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * 버튼이 두개 이고 positive 버튼 이벤트
         * negative 버튼은 dismiss 인 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param positiveBtn name
         * @param negativeBtn name
         * @param listener
         * @param isCancel
         */
        @JvmStatic
        fun showAlertDialog(context: Context, title: String, message: String, positiveBtn: String,
                            negativeBtn: String, listener: DialogInterface.OnClickListener, isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveBtn, listener)
                setNegativeButton(negativeBtn) { dialog, _ -> dialog.dismiss() }
                setCancelable(isCancel)
                show()
            }
        }


        /**
         * 버튼이 두개 이고 positive, negative 버튼 이벤트
         * OnDismissListener 가 존재하는 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param positiveBtn name
         * @param negativeBtn name
         * @param positiveListener
         * @param negativeListener
         * @param dismissListener
         * @param isCancel
         */
        @JvmStatic
        fun showAlertDialog(context: Context, title: String, message: String, positiveBtn: String,
                            negativeBtn: String, positiveListener: DialogInterface.OnClickListener?,
                            negativeListener: DialogInterface.OnClickListener?,
                            dismissListener: DialogInterface.OnDismissListener, isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveBtn, positiveListener)
                setNegativeButton(negativeBtn, negativeListener)
                setOnDismissListener(dismissListener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * 버튼이 두개 이고 positive, negative 버튼 이벤트 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param positiveBtn name
         * @param negativeBtn name
         * @param positiveListener
         * @param negativeListener
         * @param isCancel
         */
        @JvmStatic
        fun showAlertDialog(context: Context, title: String, message: String, positiveBtn: String,
                            negativeBtn: String, positiveListener: DialogInterface.OnClickListener,
                            negativeListener: DialogInterface.OnClickListener, isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(positiveBtn, positiveListener)
                setNegativeButton(negativeBtn, negativeListener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * Cube 전용 취소, 확인 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param positiveListener
         * @param negativeListener
         * @param isCancel
         */
        @JvmStatic
        fun showCubeConfirmDialog(context: Context, title: String, message: String, positiveListener: DialogInterface.OnClickListener,
                            negativeListener: DialogInterface.OnClickListener, isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(context.getString(R.string.confirm), positiveListener)
                setNegativeButton(context.getString(R.string.cancel), negativeListener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * Cube 전용 취소, 저장 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param positiveListener
         * @param negativeListener
         * @param isCancel
         */
        @JvmStatic
        fun showCubeSaveDialog(context: Context, title: String, message: String, positiveListener: DialogInterface.OnClickListener,
                                  negativeListener: DialogInterface.OnClickListener, isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(context.getString(R.string.save), positiveListener)
                setNegativeButton(context.getString(R.string.cancel), negativeListener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * Cube 전용 닫기 팝업
         *
         * @param context
         * @param title
         * @param message
         * @param listener
         * @param isCancel
         */
        @JvmStatic
        fun showCubeCloseDialog(context: Context, title: String, message: String, listener: DialogInterface.OnClickListener
                              , isCancel: Boolean) {
            AlertDialog.Builder(context).run {
                setTitle(title)
                setMessage(message)
                setPositiveButton(context.getString(R.string.close), listener)
                setCancelable(isCancel)
                show()
            }
        }

        /**
         * 리스트에서 select Index 를 알 수 있는 팝업
         *
         * @param context
         * @param title
         * @param items
         * @param positiveBtn
         * @param negativeBtn
         * @param listener
         * @param selectedIndex
         */
        @JvmStatic
        fun showSingleChoiceDialog(context: Context, title: String, items: Array<String>, positiveBtn: String,
                                   negativeBtn: String, listener: SingleChoiceDialogListener, selectedIndex: Int) {

            var itemIndex = selectedIndex
            val dialogBuilder = AlertDialog.Builder(context).apply {
                setTitle(title)
                setSingleChoiceItems(items, selectedIndex) { _, which -> itemIndex = which }
                setPositiveButton(positiveBtn) { dialog, _ ->
                    dialog.dismiss()
                    listener.onConfirm(itemIndex)
                }
                setNegativeButton(negativeBtn) { dialog, _ -> dialog.dismiss() }
            }

            val dialog = dialogBuilder.create()
            dialog.show()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            val dialogWindowHeight = (PjhUi.convertDpToPixels(
                context,
                400f
            )).toInt()
            layoutParams.height = dialogWindowHeight
            dialog.window?.attributes = layoutParams
        }

        @JvmStatic
        fun showDatePicker(context: Context, callback: DatePickerDialog.OnDateSetListener, year: Int, month: Int,
                           day: Int, positiveName: String, negativeName: String, isCancel: Boolean): DatePickerDialog {
            val datePickerDialog = DatePickerDialog(context, callback, year, month, day)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, positiveName, datePickerDialog)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, negativeName, datePickerDialog)
            datePickerDialog.setCancelable(isCancel)
            return datePickerDialog
        }

        @JvmStatic
        fun showDatePicker(context: Context, callback: DatePickerDialog.OnDateSetListener, year: Int, month: Int,
                           day: Int, positiveName: String, negativeName: String, neutralName: String, NeutralEvent: DialogInterface.OnClickListener,
                           isCancel: Boolean) {
            val datePickerDialog = DatePickerDialog(context, callback, year, month, day)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, positiveName, datePickerDialog)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, negativeName, datePickerDialog)
            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, neutralName, NeutralEvent)
            datePickerDialog.setCancelable(isCancel)
            datePickerDialog.show()
        }
    }
}