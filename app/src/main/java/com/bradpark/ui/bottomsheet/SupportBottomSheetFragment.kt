package com.bradpark.ui.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.bradpark.calllback.SupportBottomSheetStateCallback
import com.bradpark.config.PjhConfig
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.BottomSheetModuleBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.bradpark.ui.calendar.CalendarColor
import com.bradpark.ui.calendar.CalendarPagerAdapter
import com.bradpark.utils.PjhUi
import com.bradpark.ui.calendar.CubeCalendarView
import java.util.*

/**
 * 바텀 시트 프래그먼트
 *
 * @property mBottomSheetType 바텀시트 타입
 * @property mBottomSheetButtonType 바텀시트 버튼 타입
 * @property title 바텀시트 제목
 * @property mContentViewResId 바텀시트 Content Resource ID
 * @property mButtonViewResId 바텀시트 ButtonView ID
 * @property mHasFullHeight Full 화면 여부
 * @property cancelable 취소 여부
 * @property calendarType 캘린더 타입
 * @property selectDay 캘린더 선택 날짜
 * @property inActiveDay 캘린더 활성화 날짜 및 비활성화 날짜
 * @property mBottomSheetStateCallback 바텀시트 OnResume 상태
 */
class SupportBottomSheetFragment(private val mBottomSheetType: PjhConfig.PjhBottomSheetType, private val mBottomSheetButtonType: PjhConfig.PjhBottomSheetButtonType, private val title: String, private val mContentViewResId: Int, private val mButtonViewResId: Int = 0, private val mHasFullHeight: Boolean, private val cancelable: Boolean = true, private val calendarType: CalendarPagerAdapter.CalendarType = CalendarPagerAdapter.CalendarType.Normal,
                                 private val mCalendarColor: CalendarColor? = null, private val mIsCalendarHeightFixed: Boolean = false, val selectDay: Calendar? = null, val inActiveDay: Calendar? = null, private val mBottomSheetStateCallback: SupportBottomSheetStateCallback? = null, private val isMaxHeightFix : Boolean = false): BottomSheetDialogFragment() {
    companion object {
        private var mLastShowTime = 0
    }
    private lateinit var mActivity: AppCompatActivity
    private lateinit var containerView: View
    private var buttonView: View? = null
    lateinit var binding: BottomSheetModuleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.bottom_sheet_module, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        dialog.window?.navigationBarColor = PjhConfig.systemNavigationColor
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        dialog.setOnShowListener {
            val layout =
                dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            dialog.setCancelable(cancelable)
            layout.setBackgroundColor(Color.TRANSPARENT)
            layout.isNestedScrollingEnabled = true
            val params =
                layout.layoutParams as CoordinatorLayout.LayoutParams
            val displayMetrics =
                requireActivity().resources.displayMetrics
            val calcHeight = (displayMetrics.heightPixels -  PjhUi.convertDpToPixels(context!!, 64f)).toInt()
            if (mHasFullHeight) {
                params.height = calcHeight
                layout.layoutParams = params
            }
//            binding.clBottomSheetParent.maxHeight = calcHeight
            layout.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (layout.height > calcHeight) {
                        params.height = calcHeight
                        layout.layoutParams = params
                    }
                    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(layout)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.isHideable = false
                    layout.viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }

            })
        }

        return dialog
    }

    fun maxHeightSetting() {
        val layout = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        layout?.apply {
            val params = layoutParams as CoordinatorLayout.LayoutParams
            val displayMetrics = requireActivity().resources.displayMetrics
            val calcHeight = (displayMetrics.heightPixels -  PjhUi.convertDpToPixels(context!!, 64f)).toInt()
            layout.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (layout.height >= calcHeight) {
                        params.height = calcHeight
                        layout.layoutParams = params
                    }
                    layout.viewTreeObserver.removeOnPreDrawListener(this)
                    return false
                }
            })
        }
    }

    fun setInitHeight() {
        val layout = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        layout?.apply {
            val params = layoutParams as CoordinatorLayout.LayoutParams
            layout.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
            layout.layoutParams = params
        }
    }

    /**
     * BottomSheetFragment가 Resume상태에서 BottomSheetFragment를 제어하기 위함.
     */
    override fun onResume() {
        super.onResume()
        mBottomSheetStateCallback?.onResume(this,mBottomSheetType,containerView, buttonView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.CubeBottomSheetSlideAnimation
        initView()
    }

    private fun initView() {
        containerView = LayoutInflater.from(context).inflate(mContentViewResId,null)

        fun setConstraintSet(frameLayoutId: Int, changeLayoutId: Int) {
            val mConstraintSet = ConstraintSet()
            mConstraintSet.clone(binding.clBottomSheetParent)
            mConstraintSet.clear(frameLayoutId, ConstraintSet.TOP)
            mConstraintSet.connect(changeLayoutId, ConstraintSet.BOTTOM, frameLayoutId, ConstraintSet.TOP)
            mConstraintSet.connect(frameLayoutId, ConstraintSet.TOP, changeLayoutId, ConstraintSet.BOTTOM)
            mConstraintSet.applyTo(binding.clBottomSheetParent)
        }

        when (mBottomSheetType) {
            PjhConfig.PjhBottomSheetType.Calendar -> {
                (containerView as CubeCalendarView).setCalendarType(calendarType)
                mCalendarColor?.let {
                    (containerView as CubeCalendarView).setCalendarColor(it)
                }
                (containerView as CubeCalendarView).setCalendarHeightFixed(mIsCalendarHeightFixed)
                (containerView as CubeCalendarView).showView(selectDay, inActiveDay)
                binding.flBottomSheetContainer.addView(containerView)
            }
            PjhConfig.PjhBottomSheetType.ListSearch -> {
                binding.flBottomSheetContainer.visibility = View.GONE
                binding.flBottomSheetSearchContainer.visibility = View.VISIBLE
                setConstraintSet(binding.flBottomSheetBottomView.id, binding.flBottomSheetSearchContainer.id)
                binding.flBottomSheetSearchContainer.addView(containerView)
            }
            else -> {
                binding.flBottomSheetContainer.visibility = View.GONE
                binding.svBottomSheetContainer.visibility = View.VISIBLE
                setConstraintSet(binding.flBottomSheetBottomView.id, binding.svBottomSheetContainer.id)
                binding.svBottomSheetContainer.addView(containerView)
            }
        }
        binding.tvBottomSheetTitle.text = title


        if (mBottomSheetButtonType != PjhConfig.PjhBottomSheetButtonType.None) {
            buttonView = LayoutInflater.from(context).inflate(mButtonViewResId,null)
            binding.flBottomSheetBottomView.visibility = View.VISIBLE
            binding.flBottomSheetBottomView.addView(buttonView)
            (buttonView as SupportBottomSheetButtonView).setButtonType(mBottomSheetButtonType)
            (buttonView as SupportBottomSheetButtonView).setButtonStyle()
            (buttonView as SupportBottomSheetButtonView).showView()
        }
    }

    public fun getContainerView(): View {
        return containerView
    }

    public fun getBottomButtonView(): View? {
        return buttonView
    }

    /**
     * Show BottomSheetFragment
     *
     * @param activity AppCompatActivity
     * @return BottomSheetFragment?
     */
    fun show(fragmentManager: FragmentManager): SupportBottomSheetFragment? {
        if (SystemClock.elapsedRealtime() - mLastShowTime < 1000) {
            return this
        }
        mLastShowTime = SystemClock.elapsedRealtime().toInt()
        showNow(fragmentManager, tag)
        return this
    }
}