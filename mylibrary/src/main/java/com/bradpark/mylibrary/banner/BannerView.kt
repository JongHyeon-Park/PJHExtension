package com.bradpark.mylibrary.banner

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bradpark.mylibrary.R
import com.bradpark.mylibrary.databinding.ViewBannerBinding
import kotlinx.android.synthetic.main.view_banner.view.*

class BannerView @JvmOverloads constructor(
        context: Context, private val attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {
    private var enableLooping = true
    private var enableIndicator = true

    private val mBannerViewBinding: ViewBannerBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_banner, this, true)

    init {
        initView()
    }

    private fun initView() {
        mBannerViewBinding.root

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.banner_view)
        val indicatorRes = typedArray.getResourceId(R.styleable.banner_view_indicatorRes, R.drawable.banner_indicator)
        val enableSwipe = typedArray.getBoolean(R.styleable.banner_view_enableSwipe, true)
        val smoothScroll = typedArray.getBoolean(R.styleable.banner_view_smoothScroll, true)
        val rollingDelay = typedArray.getInt(R.styleable.banner_view_autoScrollDelay, 3000)
        val scrollingDelay = typedArray.getInt(R.styleable.banner_view_scrollDelay, 250)
        enableIndicator = typedArray.getBoolean(R.styleable.banner_view_enableIndicator, true)
        val indicatorMargin = typedArray.getDimensionPixelSize(R.styleable.banner_view_indicatorMargin, resources.getDimensionPixelSize(R.dimen.default_indicator_margin))
        val bottomMargin = typedArray.getDimensionPixelSize(R.styleable.banner_view_bottomMargin, resources.getDimensionPixelSize(R.dimen.default_bottom_margin))
        enableLooping = typedArray.getBoolean(R.styleable.banner_view_enableLooping, true)

        setIndicatorResources(indicatorRes, indicatorMargin)
        setEnableRolling(enableLooping)
        setEnableSwipe(enableSwipe)
        setSmoothScroll(smoothScroll)
        setRollingDelay(rollingDelay)
        setScrollingDelay(scrollingDelay)
        setBottomMargin(bottomMargin)

        typedArray.recycle()
    }

    fun <T> setAdapter(adapter: BannerViewPagerAdapter<T>) {
        adapter.enableLooping(enableLooping)

        getViewPager().adapter = adapter
        getViewPager().notifyDataSetChanged()

        indicator.setViewPager(getViewPager())
        indicator.setHideIndicator(!enableIndicator)
        indicator.notifyDataSetChanged()
    }

    fun startAutoScroll() {
        getViewPager().startAutoScroll()
    }

    fun stopAutoScroll() {
        getViewPager().stopAutoScroll()
    }

    private fun getViewPager() = mBannerViewBinding.viewPager

    private fun setIndicatorResources(resId: Int, margin: Int) {
        indicator.setIndicatorResource(resId, margin)
    }

    fun setEnableRolling(enableRolling: Boolean) {
        getViewPager().enableInfiniteSwipe(enableRolling)
    }

    private fun setEnableSwipe(enable: Boolean) {
        getViewPager().enableSwipe(enable)
    }

    private fun setSmoothScroll(smoothScroll: Boolean) {
        getViewPager().setSmoothScroll(smoothScroll)
    }

    private fun setRollingDelay(rollingDelay: Int) {
        getViewPager().setDelay(rollingDelay.toLong())
    }

    private fun setScrollingDelay(delay: Int) {
        getViewPager().setScrollDelay(delay)
    }

    private fun setBottomMargin(margin: Int) {
        val params = indicator.layoutParams as FrameLayout.LayoutParams
        params.bottomMargin = margin
        indicator.layoutParams = params
    }

}