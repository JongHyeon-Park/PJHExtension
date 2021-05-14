package com.bradpark.ui.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.CalendarModuleBinding
import java.util.*

/**
 * 캘린더
 *
 * @constructor
 *
 * @param context
 * @param attrs
 */
class CubeCalendarView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private var mDataBinding: CalendarModuleBinding? = null
    private var mCalendarType = CalendarPagerAdapter.CalendarType.Normal
    private var mClick: OnDayClick? = null
    private var mCalendar = Calendar.getInstance(PjhConfig.configTimeZone)
    private var mCalendarColor = CalendarColor()
    init {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.calendar_module, this, true)
    }
    /**
     * 캘린더 생성
     *
     * @param selectDay
     * @param inActiveDay
     */
      fun showView(selectDay: Calendar? = null, inActiveDay: Calendar? = null) {
        mDataBinding?.let { binding ->
            if (selectDay != null) {
                mCalendar = selectDay
            } else if (inActiveDay != null) {
                mCalendar = inActiveDay
            }
            binding.vpCalendar.adapter = CubeCalendarAdapter(context = binding.root.context, calendarType = mCalendarType,startCalendar = mCalendar, selectDate = selectDay, disableDay = inActiveDay
            ,selectedBackground = context.resources.getDrawable(mCalendarColor.selectedBackground,null), selectedTextColor = ContextCompat.getColor(context, mCalendarColor.selectedTextColor),
            baseTextColor = ContextCompat.getColor(context, mCalendarColor.baseTextColor), todayTextColor = ContextCompat.getColor(context, mCalendarColor.todayTextColor),
                beforeDayTextColor = ContextCompat.getColor(context, mCalendarColor.beforeDayTextColor))
            setDateText(mCalendar)
        }
         setEvent()
    }

    /**
     * 캘린더 타입 설정
     *
     * @param calendarType
     */
     fun setCalendarType(calendarType: CalendarPagerAdapter.CalendarType) {
        mCalendarType = calendarType
    }

    /**
     * 캘린더 색상 설정
     *
     * @param value
     */
    fun setCalendarColor(value: CalendarColor) {
        mCalendarColor = value
    }

    /**
     * 캘린더 높이 고정 설정
     *
     * @param value
     */
    fun setCalendarHeightFixed(value: Boolean) {
        mDataBinding?.let { binding ->
            if (value) {
                (binding.vpCalendar as? CalendarViewPager)?.setHeightFixed(value)
            }
        }
    }

    /**
     * 캘린더 Click 시 이벤트 설정
     *
     * @param onDayClickEvent
     */
    fun setClickEvent(onDayClickEvent: OnDayClick) {
        mClick = onDayClickEvent
    }

    private fun setEvent() {
        mDataBinding?.let { binding ->
            binding.vpCalendar.onCalendarChangeListener = { calendar ->
                setDateText(calendar)
            }
            binding.vpCalendar.onDayClickListener = { day, view ->
                mClick?.onClick(day, view)
            }
            binding.ivNextYear.setOnClickListener {
                binding.vpCalendar.moveItemBy(CalendarMoveData.NextYear.data)
            }
            binding.ivPreviousYear.setOnClickListener {
                binding.vpCalendar.moveItemBy(CalendarMoveData.PreviousYear.data)
            }
            binding.ivNextMonth.setOnClickListener {
                binding.vpCalendar.moveItemBy(CalendarMoveData.NextMonth.data)
            }
            binding.ivPreviousMonth.setOnClickListener {
                binding.vpCalendar.moveItemBy(CalendarMoveData.PreviousMonth.data)
            }
        }
    }

    private fun setDateText(calendar: Calendar) {
        mDataBinding?.let {
            it.tvCalendarYear.text = calendar.get(Calendar.YEAR).toString()
            it.tvCalendarMonth.text = resources.getStringArray(R.array.month_array)[calendar.get(
                Calendar.MONTH)]
        }
    }

    interface OnDayClick {
        fun onClick(day: Day, view : View)
    }

}