package com.bradpark.ui.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bradpark.calllback.CustomDialogStateCallback
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.CustomDialogModuleBinding
import com.bradpark.utils.PjhUi

/**
 * 커스텀 다이얼로그
 *
 * @property mContext context
 * @property mode 제목여부 타입
 * @property buttonType 버튼 타입
 * @property titleText 제목 텍스트
 * @property messageText 내용 텍스트
 * @property firstButtonText 첫번째 버튼 텍스트
 * @property secondButtonText 두번째 버튼 텍스트
 * @property thirdButtonText 세번째 버튼 텍스트
 * @property stateCallback 버튼 OnResume 콜백
 * @property viewId 내용 Resource Id
 * @property cancelable 취소 여부
 * @property firstButtonDrawableId 첫번째 버튼 드로어블 Resource ID
 * @property secondButtonDrawableId 두번째 버튼 드로어블 Resource ID
 * @property thirdButtonDrawableId 세번째 버튼 드로어블 Resource ID
 * @property firstButtonTextColorId 첫번째 버튼 텍스트 색상 Resource ID
 * @property secondButtonTextColorId 두번째 버튼 텍스트 색상 Resource ID
 * @property thirdButtonTextColorId 세번째 버튼 텍스트 색상 Resource ID
 * @property firstButtonStyleId 첫번째 버튼 스타일
 * @property secondButtonStyleId 두번째 버튼 스타일
 * @property thirdButtonStyleId 세번째 버튼 스타일
 */
class CustomDialog(private val mContext: Context, private val mode: PjhConfig.PjhDialogType, private val buttonType: PjhConfig.PjhDialogButtonType, private val titleText: String = "", private val messageText: String = "",
                   private val messageTextAlignment:Int = View.TEXT_ALIGNMENT_CENTER, private val firstButtonText: String = "", private val secondButtonText: String = "", private val  thirdButtonText:String = "",
                   private val stateCallback: CustomDialogStateCallback, private val viewId: Int = 0, private val cancelable :Boolean = false,
                   private val firstButtonDrawableId: Int = 0, private val secondButtonDrawableId: Int = 0, private val thirdButtonDrawableId: Int = 0,
                   private val firstButtonTextColorId:Int = 0, private val secondButtonTextColorId: Int = 0, private val thirdButtonTextColorId: Int = 0,
                   private val firstButtonStyleId: Int = 0, private val secondButtonStyleId: Int = 0, private val thirdButtonStyleId: Int = 0): DialogFragment() {
        companion object {
        const val TAG = "CubeCustomDialog"
    }
    private var mDataBinding: CustomDialogModuleBinding? = null

    private  var mCustomView: View? = null
    private  var mButtonView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.custom_dialog_module, container, false)
        return mDataBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.CubeDialogFadeAnimation
        initView()
    }


    override fun onResume() {
        super.onResume()
        stateCallback.onResumeState(this,mCustomView, mButtonView)
    }


    private fun initView() {
        isCancelable = cancelable
        changeTitle()
        setMessage()
        setCustomView()
        setButtonView()
        setMaxHeight()
    }

    private fun setMaxHeight() {
        mDataBinding?.let { binding ->
            context?.let {  context ->
                val displayHeight = context.resources.displayMetrics.heightPixels
                val margin = PjhUi.convertDpToPixels(context, 32f).toInt()
                val dialogHeight = displayHeight - (margin * 2)
                binding.clCubeDialogLayout.maxHeight = dialogHeight
            }
        }
    }

    override fun onStart() {
        super.onStart()
        context?.let { context ->
            val margin = PjhUi.convertDpToPixels(context, 32f).toInt()
            val displayWidth = context.resources.displayMetrics.widthPixels
            val dialogWidth = displayWidth - (margin * 2)
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.setLayout(dialogWidth, height)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun changeTitle() {
        mDataBinding?.let { binding ->
            context?.let { context ->
                val padding = PjhUi.convertDpToPixels(context, 24f).toInt()
                when(mode) {
                    PjhConfig.PjhDialogType.Title -> {
                        binding.tvCubeDialogTitle.visibility = View.VISIBLE
                        binding.tvCubeDialogTitle.text = titleText
                        binding.clCubeDialogContainer.setPadding(0, 0, 0, padding)
                    }
                    else -> {
                        binding.tvCubeDialogTitle.visibility = View.GONE
                        binding.clCubeDialogContainer.setPadding(0, padding, 0, padding)
                    }
                }
            }
        }
    }

    private fun setMessage() {
        mDataBinding?.let { binding ->
            if (messageText.isNotEmpty()) {
                binding.tvCubeDialogMessage.textAlignment = messageTextAlignment
                binding.tvCubeDialogMessage.visibility = View.VISIBLE
                binding.tvCubeDialogMessage.text = messageText
                binding.tvCubeDialogMessage.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (mode == PjhConfig.PjhDialogType.NoTitle && binding.tvCubeDialogMessage.lineCount == 1) {       //타이틀이 없고 한줄일때
                            binding.clCubeDialogContainer.setPadding(0, 0, 0, 0)
                        }
                        binding.tvCubeDialogMessage.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            } else {
                binding.tvCubeDialogMessage.visibility = View.GONE
            }
        }
    }

    private fun setButtonView() {
        mDataBinding?.let { binding ->
            binding.cdButtonLayout.setButtonType(buttonType)
            mButtonView = binding.cdButtonLayout
            setButtonTitle()
            setButtonDrawable()
            setButtonTextColor()
            setButtonTextStyle()
        }
    }


    private fun setButtonTitle() {
        mDataBinding?.let { binding ->
            binding.cdButtonLayout.setButtonTitle(firstTitle = firstButtonText, secondTitle = secondButtonText, thirdTitle = thirdButtonText)
        }
    }

    private fun setButtonDrawable() {
        mDataBinding?.let { binding ->
            fun checkNullDrawable(id: Int): Drawable? {
                return if (id == 0) {
                    null
                } else {
                    mContext.resources.getDrawable(id, null)
                }
            }
            when (buttonType) {
                PjhConfig.PjhDialogButtonType.One -> {
                    binding.cdButtonLayout.setFirstButtonDrawable(firstDrawable = checkNullDrawable(firstButtonDrawableId))
                }
                PjhConfig.PjhDialogButtonType.Two -> {
                    binding.cdButtonLayout.setSecondButtonDrawable(firstDrawable = checkNullDrawable(firstButtonDrawableId), secondDrawable = checkNullDrawable(secondButtonDrawableId))
                }
                PjhConfig.PjhDialogButtonType.Three -> {
                    binding.cdButtonLayout.setThirdButtonDrawable(firstDrawable = checkNullDrawable(firstButtonDrawableId), secondDrawable = checkNullDrawable(secondButtonDrawableId), thirdDrawable = checkNullDrawable(thirdButtonDrawableId))
                }
            }
        }
    }

    private fun setButtonTextColor() {
        mDataBinding?.let { binding ->
            binding.cdButtonLayout.setButtonTextColor(firstButtonTextColor = firstButtonTextColorId, secondButtonTextColor = secondButtonTextColorId, thirdButtonTextColor = thirdButtonTextColorId)
        }
    }

    private fun setButtonTextStyle() {
        mDataBinding?.let { binding ->
            binding.cdButtonLayout.setButtonTextStyle(firstTextStyleId = firstButtonStyleId, secondTextStyleId = secondButtonStyleId, thirdTextStyleId = thirdButtonStyleId)
        }
    }



    private fun setCustomView() {
        mDataBinding?.let { binding ->
            if (viewId != 0 ) {
                binding.flCubeDialogContainer.visibility = View.VISIBLE
                mCustomView = LayoutInflater.from(context).inflate(
                    viewId,
                    null,
                    false
                )
                binding.flCubeDialogContainer.addView(mCustomView)
            } else {
                binding.flCubeDialogContainer.visibility = View.GONE
            }
        }

    }

    public fun getCustomView(): View? {
        return mCustomView
    }

}