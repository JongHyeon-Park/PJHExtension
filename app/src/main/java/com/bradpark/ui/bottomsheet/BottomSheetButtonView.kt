package com.bradpark.ui.bottomsheet

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bradpark.calllback.BottomSheetButtonCallback
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.BottomSheetButtonModuleBinding
import com.bradpark.utils.PjhUi

/**
 * BottomSheetButtonView
 * BottomSheet 하단에 버튼
 *
 * @constructor
 *
 * @param context
 * @param attrs
 */
class BottomSheetButtonView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs)  {

    private var mDataBinding: BottomSheetButtonModuleBinding? = null
    private var mButtonType: PjhConfig.PjhBottomSheetButtonType = PjhConfig.PjhBottomSheetButtonType.One
    init {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.bottom_sheet_button_module, this, true)
    }

    /**
     * 버튼 타입
     *
     * @param type
     */
    fun setButtonType(type: PjhConfig.PjhBottomSheetButtonType) {
        mButtonType = type
    }

    /**
     * 오른쪽 버튼 활성화 여부
     *
     * @param enable 활성화 여부
     * @param enableDrawable 활성화 상태 드로어블
     * @param disableDrawable 비활성화 상태 드로어블
     */
    fun setRightButtonEnable(enable: PjhConfig.PjhBottomSheetButtonEnable,
                        enableDrawable: Drawable? = context.resources.getDrawable(R.drawable.radius_3_button_primary, null),
                        disableDrawable: Drawable? = context.resources.getDrawable(R.drawable.radius_3_primary_inactive, null)) {
        mDataBinding?.let { binding ->
            binding.btBottomSheetModuleRight.isClickable = enable == PjhConfig.PjhBottomSheetButtonEnable.Enable
            setRightButtonEnableUi(enableDrawable, disableDrawable)
        }
    }

    /**
     * 버튼 스타일 지정
     *
     * @param leftTextCheck 왼쪽 텍스트 색상 변경 여부
     * @param rightDrawable 오른쪽 버튼 드로어블
     * @param leftDrawableId 왼쪽 버튼 드러어블 아이디
     * @param leftTextStyleId 왼쪽 텍스트 스타일 아이디
     */
    fun setButtonStyle(leftTextCheck: Boolean = true,
                       rightDrawable: Drawable? = context.resources.getDrawable(R.drawable.radius_3_primary_inactive, null),
                       leftDrawableId: Int = R.drawable.radius_3_button_tertiary,
                       leftTextStyleId: Int = R.style.CubePrimaryTertiaryButton) {
        mDataBinding?.let { binding ->
            if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                binding.btBottomSheetModuleRight.background = rightDrawable
            } else {
                binding.btBottomSheetModuleLeft.setBackgroundResource(leftDrawableId)
                if (leftTextCheck) {
                    if (Build.VERSION.SDK_INT < 23) {
                        binding.btBottomSheetModuleLeft.setTextAppearance(context,leftTextStyleId)
                    } else {
                        binding.btBottomSheetModuleLeft.setTextAppearance(leftTextStyleId)
                    }
                }
                binding.btBottomSheetModuleRight.background = rightDrawable
            }
        }
    }

    /**
     * 버튼 이벤트
     *
     * @param buttonCallback 클릭 여부
     */
    fun setButtonEvent(buttonCallback: BottomSheetButtonCallback) {
        mDataBinding?.let { binding ->
            if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                binding.btBottomSheetModuleRight.setOnClickListener {
                    buttonCallback.onRightCallback()
                }
            } else {
                binding.btBottomSheetModuleLeft.setOnClickListener {
                    buttonCallback.onLeftCallback()
                }
                binding.btBottomSheetModuleRight.setOnClickListener {
                    buttonCallback.onRightCallback()
                }
            }
        }
    }

    /**
     * 버튼 텍스트 스타일 지정
     *
     * @param leftNotUse 왼쪽 버튼 여부
     * @param leftTextStyle 왼쪽 버튼 스타일
     * @param rightTextStyle 오른쪽 버튼 스타일
     */
    fun setButtonTextStyle(leftNotUse: Boolean = true, leftTextStyle: Int = R.style.SubTitle, rightTextStyle: Int = R.style.SubTitle) {
        mDataBinding?.let { binding ->
            if (Build.VERSION.SDK_INT < 23) {
                if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                    binding.btBottomSheetModuleRight.setTextAppearance(context,rightTextStyle)
                } else {
                    if (leftNotUse) {
                        binding.btBottomSheetModuleRight.setTextAppearance(context,rightTextStyle)
                    } else {
                        binding.btBottomSheetModuleLeft.setTextAppearance(context,leftTextStyle)
                        binding.btBottomSheetModuleRight.setTextAppearance(context,rightTextStyle)
                    }
                }
            } else {
                if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                    binding.btBottomSheetModuleRight.setTextAppearance(rightTextStyle)
                } else {
                    if (leftNotUse) {
                        binding.btBottomSheetModuleRight.setTextAppearance(rightTextStyle)
                    } else {
                        binding.btBottomSheetModuleLeft.setTextAppearance(leftTextStyle)
                        binding.btBottomSheetModuleRight.setTextAppearance(rightTextStyle)
                    }
                }
            }
        }
    }

    /**
     * 버튼 텍스트 지정
     *
     * @param leftText 왼쪽 텍스트
     * @param rightText 오른쪽 텍스트
     */
    fun setButtonText(leftText: String = "", rightText: String = "") {
        mDataBinding?.let { binding ->
            if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                binding.btBottomSheetModuleRight.text = rightText
            } else {
                binding.btBottomSheetModuleLeft.text = leftText
                binding.btBottomSheetModuleRight.text = rightText
            }
        }
    }

    /**
     * 버튼 텍스트 색상
     *
     * @param leftNotUse 왼쪽 사용 여부
     * @param leftTextColor 왼쪽 텍스트 색상
     * @param rightTextColor 오른쪽 텍스트 색상
     */
    fun setButtonTextColor(leftNotUse: Boolean = true,leftTextColor: Int = android.R.color.white, rightTextColor: Int = android.R.color.white) {
        mDataBinding?.let { binding ->
            if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                binding.btBottomSheetModuleRight.setTextColor(ContextCompat.getColor(context,rightTextColor))
            } else {
                if (leftNotUse) {
                    binding.btBottomSheetModuleRight.setTextColor(ContextCompat.getColor(context,rightTextColor))
                } else {
                    binding.btBottomSheetModuleLeft.setTextColor(ContextCompat.getColor(context,leftTextColor))
                    binding.btBottomSheetModuleRight.setTextColor(ContextCompat.getColor(context,rightTextColor))
                }
            }
        }
    }

    /**
     * 버튼 뷰 실행
     *
     * @param activity
     */
    fun showView(activity: AppCompatActivity) {
        mDataBinding?.let { binding ->
            context?.let { context ->
                if (mButtonType == PjhConfig.PjhBottomSheetButtonType.One) {
                    binding.btBottomSheetModuleLeft.visibility = View.GONE
                    val params = binding.btBottomSheetModuleRight.layoutParams as MarginLayoutParams
                    params.marginStart = PjhUi.convertDpToPixels(context, 8f).toInt()
                    binding.btBottomSheetModuleRight.layoutParams = params
                }
            }

        }

    }

    private fun setRightButtonEnableUi(enableDrawable: Drawable?, disableDrawable: Drawable?) {
        mDataBinding?.let { binding ->
            if (binding.btBottomSheetModuleRight.isClickable) {
                binding.btBottomSheetModuleRight.background = enableDrawable
            } else {
                binding.btBottomSheetModuleRight.background = disableDrawable
            }

        }
    }

}