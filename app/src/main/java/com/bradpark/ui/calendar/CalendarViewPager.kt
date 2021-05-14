package com.bradpark.ui.calendar

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bradpark.utils.PjhUi
import java.util.*

open class CalendarViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    private var enable: Boolean = true
    private var mHeight = 0
    private var mMinHeight = 0
    private var mMaxHeight = 0
    private var isFixed = false
    var onDayClickListener: ((Day, View) -> Unit)? = null
        set(value) {
            field = value
            (adapter as? CalendarPagerAdapter)?.onDayClickLister = field
        }

    var onDayLongClickListener: ((Day) -> Boolean)? = null
        set(value) {
            field = value
            (adapter as? CalendarPagerAdapter)?.onDayLongClickListener = field
        }

    var onCalendarChangeListener: ((Calendar) -> Unit)? = null

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        if (adapter is CalendarPagerAdapter) {
            this.clearOnPageChangeListeners()
            getRecyclerViewHeight()
            adapter.onDayClickLister = this.onDayClickListener
            adapter.onDayLongClickListener = this.onDayLongClickListener
            adapter.setInitCalendarItem()
            setRecyclerViewHeight(getCurrentCalendar()!!)
            setCurrentItem(CalendarPagerAdapter.MAX_VALUE / 2, false)
            this.addOnPageChangeListener(pageChangeListener)
        }
    }

    fun setHeightFixed(fixed: Boolean) {
        this.isFixed = fixed
    }

    private fun getRecyclerViewHeight() {
        val temp = 49
        this.mMinHeight = PjhUi.convertDpToPixels(context, (temp * 5).toFloat()).toInt()
        this.mMaxHeight = PjhUi.convertDpToPixels(context, (temp * 6).toFloat()).toInt()
    }

    fun setRecyclerViewHeight(calendar: Calendar) {
        var weekOfMonth = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + (if (CalendarPagerAdapter.DayOfWeek.Sunday.isLessFirstWeek(calendar)) 1 else 0) - (if (CalendarPagerAdapter.DayOfWeek.Sunday.isMoreLastWeek(calendar)) 1 else 0)
        weekOfMonth *= 7
        mHeight = if (weekOfMonth == 42) {  // 6줄 일때
            mMaxHeight
        } else {
            if (isFixed) {
                mMaxHeight
            } else {
                mMinHeight
            }

        }
        requestLayout()
    }
    //문제
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // initialized child views
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // support wrap_content
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        if (mode == MeasureSpec.AT_MOST) {
            val exactlyHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, exactlyHeightMeasureSpec)
        }
    }

    fun getCurrentCalendar(): Calendar? = (adapter as? CalendarPagerAdapter)?.getCalendar(currentItem)

    fun moveItemBy(position: Int, smoothScroll: Boolean = true) {
        if (position != 0) {
            setCurrentItem(currentItem + position, smoothScroll)
        }
    }

    private val pageChangeListener = object : OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            val calendar = (adapter as? CalendarPagerAdapter)?.getCalendar(position) ?: return
            onCalendarChangeListener?.invoke(calendar)
            setRecyclerViewHeight(calendar)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (enable) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (enable) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

    fun setPagingEnabled(enabled: Boolean ) {
        this.enable = enabled;
    }
}
