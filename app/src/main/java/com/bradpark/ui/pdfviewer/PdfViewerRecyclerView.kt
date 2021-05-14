package com.bradpark.ui.pdfviewer

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bradpark.ui.pdfviewer.interfaces.OnPageChangedListener
import com.bradpark.ui.pdfviewer.interfaces.OnScrollStateChangeListener
import java.lang.Exception
import kotlin.math.max
import kotlin.math.min

class PdfViewerRecyclerView(context: Context, attrs: AttributeSet? = null): RecyclerView(context, attrs) {

    private val INVALID_POINTER_ID = -1

    private var mActivePointerId = INVALID_POINTER_ID

    private var mScaleGestureDetector: ScaleGestureDetector

    private var mScaleFactor: Float = 1f

    private var mMaxWidth: Float = 0f

    private var mMaxHeight: Float = 0f

    private var mLastTouchX: Float = 0f

    private var mLastTouchY: Float = 0f

    private var mTouchX: Float = 0f

    private var mTouchY: Float = 0f

    private var mWidth: Float = 0f

    private var mHeight: Float = 0f

    private var mIsZoomEnabled: Boolean = false

    private var mMaxZoom: Float = 3f

    private var mMinZoom: Float = 1f

    private var mOnPageChangedListener: OnPageChangedListener? = null

    private var mPosition = -1

    private var mOnScrollStateChangeListener: OnScrollStateChangeListener? = null

    init {
        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

    }

    fun setZoomEnabled(isZoomEnabled: Boolean) {
        this.mIsZoomEnabled = isZoomEnabled
    }

    fun setMaxZoom(maxZoom: Float) {
        this.mMaxZoom = maxZoom
    }

    fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener?) {
        this.mOnPageChangedListener = onPageChangedListener
    }

    fun setOnScrollStateChangeListener(onScrollStateChangeListener: OnScrollStateChangeListener?) {
        this.mOnScrollStateChangeListener = onScrollStateChangeListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        super.onTouchEvent(ev)
        performClick()
        val action = ev.action
        mScaleGestureDetector.onTouchEvent(ev)
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = ev.x
                mLastTouchY = ev.y
                mActivePointerId = ev.getPointerId(0)
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val x = ev.getX(pointerIndex)
                val y = ev.getY(pointerIndex)
                val dx = x - mLastTouchX
                val dy = y - mLastTouchY

                mTouchX += dx
                mTouchY += dy

                if (mTouchX > 0f)
                    mTouchX = 0f
                else if (mTouchX < mMaxWidth)
                    mTouchX = mMaxWidth

                if (mTouchY > 0f)
                    mTouchY = 0f
                else if (mTouchY < mMaxHeight)
                    mTouchY = mMaxHeight

                mLastTouchX = x
                mLastTouchY = y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
            }

            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastTouchX = ev.getX(newPointerIndex)
                    mLastTouchY = ev.getY(newPointerIndex)
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                }
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(mTouchX, mTouchY)
        canvas.scale(mScaleFactor, mScaleFactor)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        if (mScaleFactor == mMinZoom) {
            mTouchX = 0f
            mTouchY = 0f
        }

        canvas.translate(mTouchX, mTouchY)
        canvas.scale(mScaleFactor, mScaleFactor)

        super.dispatchDraw(canvas)

        canvas.restore()
        invalidate()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (mIsZoomEnabled) {
                mScaleFactor *= detector.scaleFactor
                mScaleFactor = max(mMinZoom, min(mScaleFactor, mMaxZoom))
                mMaxWidth = mWidth - (mWidth * mScaleFactor)
                mMaxHeight = mHeight - (mHeight * mScaleFactor)
                invalidate()
            }
            return true
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val position = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if(position != mPosition && position != -1) {
            mPosition = position
            mOnPageChangedListener?.onPageChanged(mPosition + 1, adapter?.itemCount ?: 0)
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        mOnScrollStateChangeListener?.onScrollState(state)
    }
}

enum class PdfScrollState(val index: Int) {
    Dragging(1), Idle(0), Fling(2);
}