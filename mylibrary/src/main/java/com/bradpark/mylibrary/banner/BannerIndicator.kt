package com.siwonschool.ui.banner

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bradpark.mylibrary.R

class BannerIndicator @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var margin = 10
    private var resId = View.NO_ID
    private lateinit var viewPager: androidx.viewpager.widget.ViewPager
    private val adapter: BannerViewPagerAdapter<*>
        get() = viewPager.adapter as BannerViewPagerAdapter<*>
    private var hideIndicator = false

    fun setIndicatorResource(resId: Int, margin: Int) {
        this.margin = margin
        this.resId = resId
    }

    fun setViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        this.viewPager = viewPager

        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                selectIndicator(adapter.getRealPosition(position))
                if (viewPager is BannerViewPager) {
                    viewPager.startAutoScroll()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun selectIndicator(position: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isSelected = i == position
        }
    }

    fun setHideIndicator(hideIndicator: Boolean) {
        this.hideIndicator = hideIndicator
    }

    fun notifyDataSetChanged() {
        removeAllViews()

        if (hideIndicator) return

        if (adapter.realCount < 2) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            addIndicator()
        }
    }

    private fun addIndicator() {
        val currentPosition = adapter.getRealPosition(viewPager.currentItem)
        for (i in 0 until adapter.realCount) {
            val imageView = ImageView(context)
            if (resId == View.NO_ID) {
                imageView.setImageResource(R.drawable.banner_indicator)
            } else {
                imageView.setImageResource(resId)
            }

            if (currentPosition == i) {
                imageView.isSelected = true
            }

            if (i != adapter.realCount - 1) {
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.rightMargin = margin
                imageView.layoutParams = params
            }

            imageView.setOnClickListener { viewPager.currentItem = i }
            addView(imageView)
        }
    }

}