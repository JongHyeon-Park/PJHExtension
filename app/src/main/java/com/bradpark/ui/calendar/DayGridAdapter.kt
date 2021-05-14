package com.bradpark.ui.calendar

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bradpark.config.PjhConfig
import org.apache.commons.lang3.time.DateUtils

import java.util.*
import kotlin.properties.Delegates

abstract class CalendarCellAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val context: Context
    private val calendar: Calendar
    private var weekOfMonth: Int
    private val startDate: Calendar
    private val type: CalendarPagerAdapter.CalendarType
    private val mSelectCalendar: Calendar?
    private val mInActiveDay: Calendar?
    var items: List<Day> by Delegates.observable(emptyList()) { _, old, new ->
        CalendarDiff(old, new).calculateDiff().dispatchUpdatesTo(this)
    }

    constructor(context: Context, date: Date, preselectedDay: Date? = null) : this(context, Calendar.getInstance().apply { time = date }, CalendarPagerAdapter.DayOfWeek.Sunday, preselectedDay)

    constructor(context: Context, calendar: Calendar, startingAt: CalendarPagerAdapter.DayOfWeek, preselectedDay: Date? = null, calendarType: CalendarPagerAdapter.CalendarType = CalendarPagerAdapter.CalendarType.Normal, calendarSelect: Calendar? = null, inActiveDay: Calendar? = null) : super() {
        this.context = context
        this.calendar = calendar
        this.type = calendarType
        mSelectCalendar = calendarSelect
        mInActiveDay = inActiveDay
        val start = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH)
        if (start.get(Calendar.DAY_OF_WEEK) != (startingAt.getDifference() + 1)) {
            start.set(Calendar.DAY_OF_MONTH, if (startingAt.isLessFirstWeek(calendar)) -startingAt.getDifference() else 0)
            start.add(Calendar.DAY_OF_MONTH, -start.get(Calendar.DAY_OF_WEEK) + 1 + startingAt.getDifference())
        }
        startDate = start
        this.weekOfMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + (if (startingAt.isLessFirstWeek(calendar)) 1 else 0) - (if (startingAt.isMoreLastWeek(calendar)) 1 else 0)
        this.weekOfMonth *= 7

        updateItems(preselectedDay)
    }

    fun updateItems(selectedDate: Date? = null) {
        val nowCalendar = Calendar.getInstance()
        if (nowCalendar.timeZone != PjhConfig.configTimeZone) {
            nowCalendar.timeZone = PjhConfig.configTimeZone
        }

        val calendarValue = when(type) {
            CalendarPagerAdapter.CalendarType.Normal -> {
                Calendar.getInstance()
            }
            CalendarPagerAdapter.CalendarType.To -> {
                mInActiveDay ?: Calendar.getInstance()
            }
            CalendarPagerAdapter.CalendarType.From -> {
                mInActiveDay ?: Calendar.getInstance()
            } else -> {
                Calendar.getInstance()
            }
        }
        items = (0..itemCount).map {
            val cal = Calendar.getInstance().apply {
                time = startDate.time
                timeZone = PjhConfig.configTimeZone
            }
            cal.add(Calendar.DAY_OF_MONTH, it)

            val thisTime = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH)
            val compareTime = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)

            val state = when (thisTime.compareTo(compareTime)) {
                -1 -> MonthState.NextMonth
                0 -> MonthState.ThisMonth
                1 -> MonthState.PreviousMonth
                else -> throw IllegalStateException()
            }
            var calendarType = when (calendarValue.get(Calendar.DAY_OF_YEAR).compareTo(cal.get(Calendar.DAY_OF_YEAR))) {
                -1 -> DayState.NextDay
                0 -> {
                    DayState.ThisDay
                }
                1 -> DayState.PreviousDay
                else -> throw IllegalStateException()
            }
            if (calendarValue.get(Calendar.YEAR) < cal.get(Calendar.YEAR)) {
                calendarType = DayState.NextDay
            } else if (calendarValue.get(Calendar.YEAR) > cal.get(Calendar.YEAR)) {
                calendarType = DayState.PreviousDay
            }
            val isSelected = when (selectedDate) {
                null -> false
                else -> DateUtils.isSameDay(cal.time, selectedDate)
            }
            val isToday = DateUtils.isSameDay(cal, nowCalendar)

            Day(cal, state, isToday, isSelected, calendarType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, items[holder.layoutPosition])
    }

    override fun getItemCount(): Int = this.weekOfMonth

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, day: Day)
}

data class Day(
    var calendar: Calendar,
    var state: MonthState,
    var isToday: Boolean,
    var isSelected: Boolean,
    var dayState: DayState
)

enum class MonthState {
    PreviousMonth,
    ThisMonth,
    NextMonth
}

enum class DayState {
    PreviousDay,
    ThisDay,
    NextDay
}

enum class CalendarMoveData(val data: Int) {
    PreviousYear(-12),
    PreviousMonth(-1),
    NextYear(12),
    NextMonth(1)
}
