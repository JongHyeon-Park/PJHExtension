package com.bradpark.ui.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bradpark.calllback.CustomDialogCallback
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.DialogButtonModuleBinding
import com.bradpark.utils.PjhUi

/**
 * 커스텀 다이얼로그 하단 버튼 뷰
 *
 * @constructor
 *
 * @param context
 * @param attrs
 */
class DialogButtonView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs)  {
    private var mDataBinding: DialogButtonModuleBinding? = null
    private var mButtonType = PjhConfig.PjhDialogButtonType.One
    init {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_button_module, this, true)
    }

    /**
     * 다이얼로그 버튼 타입
     *
     * @param type
     */
    fun setButtonType(type: PjhConfig.PjhDialogButtonType) {
        mButtonType = type
        setButtonLayoutOrientation()
        setButtonVisible()

    }

    private fun setButtonVisible() {
        mDataBinding?.let { binding ->
            when (mButtonType) {
                PjhConfig.PjhDialogButtonType.One -> {
                    binding.btBottomSheetModuleFirst.visibility = View.VISIBLE
                    binding.btBottomSheetModuleSecond.visibility = View.GONE
                    binding.btBottomSheetModuleThird.visibility = View.GONE
                }
                PjhConfig.PjhDialogButtonType.Two -> {
                    binding.btBottomSheetModuleFirst.visibility = View.VISIBLE
                    binding.btBottomSheetModuleSecond.visibility = View.VISIBLE
                    binding.btBottomSheetModuleThird.visibility = View.GONE
                }
                PjhConfig.PjhDialogButtonType.Three -> {
                    binding.btBottomSheetModuleFirst.visibility = View.VISIBLE
                    binding.btBottomSheetModuleSecond.visibility = View.VISIBLE
                    binding.btBottomSheetModuleThird.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * 다이얼로그 버튼 텍스트
     *
     * @param firstTitle 첫번째 텍스트
     * @param secondTitle 두번째 텍스트
     * @param thirdTitle 세번째 텍스트
     */
    fun setButtonTitle(firstTitle: String = "", secondTitle: String = "", thirdTitle: String = "") {
        mDataBinding?.let { binding ->
            when (mButtonType) {
                PjhConfig.PjhDialogButtonType.One -> {
                    binding.btBottomSheetModuleFirst.text = firstTitle
                }
                PjhConfig.PjhDialogButtonType.Two -> {
                    binding.btBottomSheetModuleFirst.text = firstTitle
                    binding.btBottomSheetModuleSecond.text = secondTitle
                }
                PjhConfig.PjhDialogButtonType.Three -> {
                    binding.btBottomSheetModuleFirst.text = firstTitle
                    binding.btBottomSheetModuleSecond.text = secondTitle
                    binding.btBottomSheetModuleThird.text = thirdTitle
                }
            }
        }
    }

    /**
     * 다이얼로그 버튼 클릭 이벤트
     *
     * @param event
     */
    fun setButtonClickEvent(event: CustomDialogCallback) {
        mDataBinding?.let { binding ->
            binding.btBottomSheetModuleFirst.setOnClickListener {
                event.onFirstClick()
            }
            binding.btBottomSheetModuleSecond.setOnClickListener {
                event.onSecondClick()
            }
            binding.btBottomSheetModuleThird.setOnClickListener {
                event.onThirdClick()
            }
        }
    }

    /**
     * 다이얼로그 버튼 텍스트 스타일
     *
     * @param firstTextStyleId 첫번째 버튼 텍스트 스타일 Resource ID
     * @param secondTextStyleId 두번째 버튼 텍스트 스타일 Resource ID
     * @param thirdTextStyleId 세번째 버튼 텍스트 스타일 Resource ID
     */
    fun setButtonTextStyle(firstTextStyleId: Int , secondTextStyleId: Int , thirdTextStyleId: Int ) {
        mDataBinding?.let { binding ->
            fun checkTextStyle(id: Int): Int {
                return if (id == 0) {
                    R.style.SubTitle
                } else {
                    id
                }
            }
            if (Build.VERSION.SDK_INT < 23) {
                when (mButtonType) {
                    PjhConfig.PjhDialogButtonType.One -> {
                        binding.btBottomSheetModuleFirst.setTextAppearance(context,checkTextStyle(firstTextStyleId))
                    }
                    PjhConfig.PjhDialogButtonType.Two -> {
                        binding.btBottomSheetModuleFirst.setTextAppearance(context,checkTextStyle(firstTextStyleId))
                        binding.btBottomSheetModuleSecond.setTextAppearance(context,checkTextStyle(secondTextStyleId))
                    }
                    PjhConfig.PjhDialogButtonType.Three -> {
                        binding.btBottomSheetModuleFirst.setTextAppearance(context,checkTextStyle(firstTextStyleId))
                        binding.btBottomSheetModuleSecond.setTextAppearance(context,checkTextStyle(secondTextStyleId))
                        binding.btBottomSheetModuleThird.setTextAppearance(context,checkTextStyle(thirdTextStyleId))
                    }
                }
            } else {
                when (mButtonType) {
                    PjhConfig.PjhDialogButtonType.One -> {
                        binding.btBottomSheetModuleFirst.setTextAppearance(checkTextStyle(firstTextStyleId))
                    }
                    PjhConfig.PjhDialogButtonType.Two -> {
                        binding.btBottomSheetModuleFirst.setTextAppearance(checkTextStyle(firstTextStyleId))
                        binding.btBottomSheetModuleSecond.setTextAppearance(checkTextStyle(secondTextStyleId))
                    }
                    PjhConfig.PjhDialogButtonType.Three -> {
                        binding.btBottomSheetModuleFirst.setTextAppearance(checkTextStyle(firstTextStyleId))
                        binding.btBottomSheetModuleSecond.setTextAppearance(checkTextStyle(secondTextStyleId))
                        binding.btBottomSheetModuleThird.setTextAppearance(checkTextStyle(thirdTextStyleId))
                    }
                }
            }
        }
    }

    /**
     * 다이얼로그 버튼 텍스트 색상
     *
     * @param firstButtonTextColor 첫번째 버튼 텍스트 색상
     * @param secondButtonTextColor 두번째 버튼 텍스트 색상
     * @param thirdButtonTextColor 세번째 버튼 텍스트 색상
     */
    fun setButtonTextColor(firstButtonTextColor: Int = 0, secondButtonTextColor: Int = 0, thirdButtonTextColor: Int = 0) {
        mDataBinding?.let { binding ->
            fun checkColorId(id: Int): Int {
                return if (id == 0) {
                    R.color.colorWhite
                } else {
                    id
                }
            }
            when (mButtonType) {
                PjhConfig.PjhDialogButtonType.One -> {
                    binding.btBottomSheetModuleFirst.setTextColor(ContextCompat.getColor(context,checkColorId(firstButtonTextColor)))
                }
                PjhConfig.PjhDialogButtonType.Two -> {
                    binding.btBottomSheetModuleFirst.setTextColor(ContextCompat.getColor(context,checkColorId(firstButtonTextColor)))
                    binding.btBottomSheetModuleSecond.setTextColor(ContextCompat.getColor(context,checkColorId(secondButtonTextColor)))
                }
                PjhConfig.PjhDialogButtonType.Three -> {
                    binding.btBottomSheetModuleFirst.setTextColor(ContextCompat.getColor(context,checkColorId(firstButtonTextColor)))
                    binding.btBottomSheetModuleSecond.setTextColor(ContextCompat.getColor(context,checkColorId(secondButtonTextColor)))
                    binding.btBottomSheetModuleThird.setTextColor(ContextCompat.getColor(context,checkColorId(thirdButtonTextColor)))
                }
            }
        }

    }

    fun setFirstButtonDrawable(firstDrawable: Drawable?) {
        mDataBinding?.let { binding ->
            binding.btBottomSheetModuleFirst.background = firstDrawable ?: context.resources.getDrawable(R.drawable.radius_4_bottom_primary_click, null)
        }
    }


     fun setSecondButtonDrawable(firstDrawable: Drawable?, secondDrawable: Drawable?) {
        mDataBinding?.let { binding ->
            binding.btBottomSheetModuleFirst.background = firstDrawable ?: context.resources.getDrawable(R.drawable.radius_4_bottom_left_secondary_click, null)
            binding.btBottomSheetModuleSecond.background = secondDrawable ?: context.resources.getDrawable(R.drawable.radius_4_bottom_right_primary_click, null)
        }
    }

     fun setThirdButtonDrawable( firstDrawable: Drawable?, secondDrawable: Drawable?, thirdDrawable: Drawable?) {
        mDataBinding?.let { binding ->
            val height = PjhUi.convertDpToPixels(context, 48f).toInt()
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            binding.btBottomSheetModuleFirst.layoutParams = layoutParams
            binding.btBottomSheetModuleFirst.background = firstDrawable ?: context.resources.getDrawable(R.drawable.primary_click, null)
            binding.btBottomSheetModuleSecond.layoutParams = layoutParams
            binding.btBottomSheetModuleSecond.background = secondDrawable ?: context.resources.getDrawable(R.drawable.danger_click, null)
            binding.btBottomSheetModuleThird.layoutParams = layoutParams
            binding.btBottomSheetModuleThird.background = thirdDrawable ?: context.resources.getDrawable(R.drawable.radius_4_bottom_secondary_click, null)
        }
    }

    private fun setButtonLayoutOrientation() {
        mDataBinding?.let { binding ->
            if (mButtonType == PjhConfig.PjhDialogButtonType.Three) {
                binding.llCubeDialogButtonLayout.orientation = LinearLayout.VERTICAL
            } else {
                binding.llCubeDialogButtonLayout.orientation = LinearLayout.HORIZONTAL
            }
        }
    }
}