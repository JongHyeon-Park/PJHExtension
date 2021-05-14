package com.bradpark.ui.snackbar

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.google.android.material.snackbar.ContentViewCallback

class PjhSnackBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {


    init {
        View.inflate(context, R.layout.view_cubesnackbar, this)
        clipToPadding = false

    }

    fun setData(mode: PjhConfig.PjhSnackBarType, message: String, iconId: Int  = 0) {
        val constraint = findViewById<ConstraintLayout>(R.id.cl_snack_layout)
        val tvTitle = findViewById<TextView>(R.id.tv_snack_title)
        val tvMessage = findViewById<TextView>(R.id.tv_snack_message)
        val ivIcon = findViewById<ImageView>(R.id.iv_snack_view)
        if (iconId != 0) {
            ivIcon.setBackgroundResource(iconId)
        }
        constraint.setBackgroundResource(R.drawable.radius_4)
        val drawable = constraint.background as GradientDrawable
        when (mode) {
            PjhConfig.PjhSnackBarType.SUCCESS -> {
                drawable.setColor(ContextCompat.getColor(context, R.color.snack_success))
                tvTitle.text = context.getString(R.string.snack_success)
            }
            PjhConfig.PjhSnackBarType.FAIL -> {
                drawable.setColor(ContextCompat.getColor(context, R.color.red01))
                tvTitle.text = context.getString(R.string.snack_fail)
            }
            PjhConfig.PjhSnackBarType.INFORMATION -> {
                drawable.setColor(ContextCompat.getColor(context, R.color.blue03))
                tvTitle.text = context.getString(R.string.snack_information)
            }
            PjhConfig.PjhSnackBarType.WARNING -> {
                drawable.setColor(ContextCompat.getColor(context, R.color.orange))
                tvTitle.text = context.getString(R.string.snack_waring)
            }
        }
        tvMessage.text = message
    }

    override fun animateContentIn(delay: Int, duration: Int) {
//        val scaleX = ObjectAnimator.ofFloat(chefImage, View.SCALE_X, 0f, 1f)
//        val scaleY = ObjectAnimator.ofFloat(chefImage, View.SCALE_Y, 0f, 1f)
//        val animatorSet = AnimatorSet().apply {
//            interpolator = OvershootInterpolator()
//            setDuration(500)
//            playTogether(scaleX, scaleY)
//        }
//        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}