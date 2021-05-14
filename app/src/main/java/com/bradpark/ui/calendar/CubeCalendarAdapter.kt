package com.bradpark.ui.calendar

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bradpark.pjhextension.R
import java.util.*

class CubeCalendarAdapter(context: Context, calendarType: CalendarType = CalendarType.Normal, private val selectedBackground: Drawable = context.resources.getDrawable(R.drawable.radius_6_maincolor,null),
                          private val selectedTextColor: Int = ContextCompat.getColor(context, R.color.colorWhite), private val baseTextColor: Int =  ContextCompat.getColor(context, R.color.gray08),
                          private val todayTextColor: Int = ContextCompat.getColor(context, R.color.blue02), private val beforeDayTextColor: Int = ContextCompat.getColor(context, R.color.gray05),
                          private val startCalendar: Calendar?, private val selectDate: Calendar?, private val disableDay: Calendar?): CalendarPagerAdapter(context, calendarType = calendarType, base = startCalendar, selectCalendar = selectDate, inActiveDay = disableDay) {
    override fun onCreateView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.calendar_day_text_module, parent, false)
    }

    override fun onBindView(view: View, day: Day) {
        if (day.state == MonthState.ThisMonth) {
            view.visibility = View.VISIBLE
            val dayText = view.findViewById<TextView>(R.id.tv_calendar_day_text)
            dayText.text = day.calendar.get(Calendar.DAY_OF_MONTH).toString()
            if (day.isToday) {
                dayText.setTextColor(todayTextColor)
            }
            if (day.isSelected ) {
                dayText.background = selectedBackground
                dayText.setTextColor(selectedTextColor)
            } else {
                dayText.background = null
                if (!day.isToday) {
                    dayText.setTextColor(baseTextColor)
                } else {
                    dayText.setTextColor(todayTextColor)
                }
            }
            setText(dayText, day)
        } else {
            view.visibility = View.GONE
        }
    }

    private fun setText(textView: TextView, day: Day) {
        disableDay?.let {
            when (calendarType) {
                CalendarType.To -> {
                    if (day.dayState == DayState.NextDay) {
                        textView.setTextColor(beforeDayTextColor)
                    }
                }
                CalendarType.From -> {
                    if (day.dayState == DayState.PreviousDay) {
                        textView.setTextColor(beforeDayTextColor)
                    }
                }
                else -> {

                }
            }
        }
        if (calendarType == CalendarType.Abnormal) {
            if (day.dayState == DayState.PreviousDay) {
                textView.setTextColor(beforeDayTextColor)
            }
        }
    }
}