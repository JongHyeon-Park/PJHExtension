package com.bradpark.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R

class PjhTimePickerFragment(listener: OnTimeSetListener?, hour: Int, minute: Int, color: Int = -1): DialogFragment() {
    private val TAG = "PjhTimePickerFragment"

    private var mListener: OnTimeSetListener? = listener
    private var mHour = hour
    private var mMinute = minute
    private val mColor = color



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = TimePickerDialog(
            context, R.style.TimePicker,
            mListener, mHour, mMinute, DateFormat.is24HourFormat(context)
        )
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
        setTimePickerDividerColor(dialog)
        PjhUi.applyPopupButtonStyle(dialog)
        return dialog
    }

    private fun setTimePickerDividerColor(timePickerDialog: TimePickerDialog) {
        var timePicker: TimePicker? = null
        try {
            val timePickerField =
                timePickerDialog.javaClass.getDeclaredField("mTimePicker")
            timePickerField.isAccessible = true
            timePicker = timePickerField[timePickerDialog] as TimePicker
        } catch (ignored: Exception) {
        }
        if (timePicker == null) {
            return
        }
        val system = Resources.getSystem()
        val hourNumberPickerId = system.getIdentifier("hour", "id", "android")
        val minuteNumberPickerId = system.getIdentifier("minute", "id", "android")
        val amPmNumberPickerId = system.getIdentifier("amPm", "id", "android")
        val numberPickers = arrayOf(
            timePicker.findViewById(hourNumberPickerId),
            timePicker.findViewById(minuteNumberPickerId),
            timePicker.findViewById<NumberPicker>(amPmNumberPickerId)
        )
        try {
            val numberPickerClass =
                Class.forName("android.widget.NumberPicker")
            val dividerField =
                numberPickerClass.getDeclaredField("mSelectionDivider")
            dividerField.isAccessible = true
            val color = if(mColor == -1) {
                PjhConfig.mainColor
            } else {
                mColor
            }
            val colorDrawable =
                ColorDrawable(color)
            for (numberPicker in numberPickers) {
                if (numberPicker != null) {
                    dividerField[numberPicker] = colorDrawable
                    numberPicker.invalidate()
                }
            }
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "ClassNotFoundException in TimePickerFragment", e)
        } catch (e: NoSuchFieldException) {
            Log.e(TAG, "NoSuchFieldException in TimePickerFragment", e)
        } catch (e: IllegalAccessException) {
            Log.e(TAG, "IllegalAccessException in TimePickerFragment", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "IllegalArgumentException in TimePickerFragment", e)
        }
    }
}