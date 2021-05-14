package com.bradpark.adapter

import androidx.databinding.BindingAdapter
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class CustomBinder {

    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(view: ImageView, url: String?) {
            url?.let {
                Glide.with(view.context)
                        .load(it)
                        .apply(RequestOptions().fitCenter())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(view)
            }
        }


        @JvmStatic
        @BindingAdapter("imageRes")
        fun setImageRes(view: ImageView, resId: Int?) {
            resId?.let {
                Glide.with(view.context)
                        .load(resId)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("foregroundColorSpanText", "foregroundColorSpanColor")
        fun setForegroundColorSpan(textView: TextView, foregroundColorSpanText: String?, foregroundColorSpanColor: Int) {
            foregroundColorSpanText?.let {
                val builder = SpannableStringBuilder(textView.text)
                val startPos = textView.text.indexOf(foregroundColorSpanText)

                if (startPos >= 0) {
                    val endPos = startPos + foregroundColorSpanText.length
                    builder.setSpan(ForegroundColorSpan(foregroundColorSpanColor), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.text = builder
                }
            }
        }

        @JvmStatic
        @BindingAdapter("badgeCnt")
        fun setBadgeCnt(textView: TextView, count: String) {
            Log.d("Test", "count : $count")
            textView.text = count
            if (count == "0" || TextUtils.isEmpty(count)) {
                textView.visibility = View.GONE
            } else {
                textView.visibility = View.VISIBLE
            }
        }

        @JvmStatic
        @BindingAdapter("toolbarText")
        fun setToolbarTitle(textView: TextView, title: String) {
            textView.visibility = View.GONE
            textView.post {
                if (!TextUtils.isEmpty(title)) {
                    textView.visibility = View.VISIBLE
                }
            }

            textView.text = title
        }

        @JvmStatic
        @BindingAdapter("siteName")
        fun setSiteNameText(textView: TextView, siteCode: String) {
            val textRes = textView.context.resources.getIdentifier("site_$siteCode", "string", textView.context.packageName)
            val textBackgroundRes = textView.context.resources.getIdentifier("round_box_$siteCode", "drawable", textView.context.packageName)
            textView.setText(textRes)
            textView.setBackgroundResource(textBackgroundRes)
        }

        @JvmStatic
        @BindingAdapter("siteLogo")
        fun setSiteLogo(imageView: ImageView, siteCode: String) {
            val imgRes = imageView.context.resources.getIdentifier("logo_$siteCode", "drawable", imageView.context.packageName)
            imageView.setImageResource(imgRes)
        }

//        @JvmStatic
//        @BindingAdapter("siteUrlLogoSiteCode","siteUrlLogoUrl")
//        fun setSiteUrlLogo(imageView: ImageView, siteUrlLogoSiteCode: String, siteUrlLogoUrl: String?) {
//            if(siteUrlLogoUrl.isNullOrEmpty()) {
//                val imgRes = imageView.context.resources.getIdentifier("logo_$siteUrlLogoSiteCode", "drawable", imageView.context.packageName)
//                imageView.setImageResource(imgRes)
//            } else {
//                siteUrlLogoUrl?.let {
//                    Glide.with(imageView.context)
//                            .load(it)
//                            .apply(RequestOptions().fitCenter())
//                            .placeholder(com.siwonschool.siwonlectureroom.R.drawable.logo_basics)
//                            .error(imageView.context.resources.getIdentifier("logo_$siteUrlLogoSiteCode", "drawable", imageView.context.packageName))
//                            .into(imageView)
//                }
//            }
//        }

        @JvmStatic
        @BindingAdapter("visibleYN")
        fun setVisibleYN(view: View, isVisible: String) {
            if (isVisible.equals("Y", true)) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("customVisible")
        fun setCustomVisible(view: View, isVisible: Boolean) {
            if (isVisible) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("customVisibleReverse")
        fun setCustomVisibleReverse(view: View, isVisible: Boolean) {
            if (isVisible) {
                view.visibility = View.INVISIBLE
            } else {
                view.visibility = View.VISIBLE
            }
        }

        @JvmStatic
        @BindingAdapter("customVisibleReverse2")
        fun setCustomVisibleReverse2(view: View, isVisible: Boolean) {
            if (isVisible) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        }

        @JvmStatic
        @BindingAdapter("visibleEmpty")
        fun setVisibleEmpty(view: View, isVisible: String?) {
            isVisible?.apply {
                if (this.trim().isEmpty()) {
                    view.visibility = View.GONE
                } else {
                    view.visibility = View.VISIBLE
                }
                return
            }.let {
                view.visibility = View.GONE
            }
        }


        @JvmStatic
        @BindingAdapter("customTextColor")
        fun setTextColor(view: TextView, color: Int) {
            view.setTextColor(color)
        }



        @JvmStatic
        @BindingAdapter("customProgressRate")
        fun setProgressRate(progressbar: ProgressBar, rate: Int) {
            progressbar.progress = if (rate <= 100) rate else 100
        }
    }

}