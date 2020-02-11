package com.siwonschool.ui.banner

import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup

abstract class BannerViewPagerAdapter<T>(private val itemList: ArrayList<T>) : androidx.viewpager.widget.PagerAdapter() {

    private var placeHolderCount = 100000
    private var lastEnableLooping = true
    val realCount: Int
        get() = this.itemList.size

    override fun getCount(): Int {
        return itemList.size * placeHolderCount
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = this.getView(getRealPosition(position), getItem(getRealPosition(position)))
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(pager: View, obj: Any): Boolean {
        return pager === obj
    }

    private fun getItem(position: Int): T {
        return this.itemList[position]
    }

    fun enableLooping(enabled: Boolean) {
        lastEnableLooping = enabled
        placeHolderCount = if (enabled) {
            100000
        } else {
            1
        }
    }

    fun getRealPosition(page: Int) = page % realCount

    abstract fun getView(position: Int, item: T): View
}