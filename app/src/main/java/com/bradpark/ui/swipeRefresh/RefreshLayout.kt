package com.bradpark.ui.swipeRefresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.RefreshModuleBinding
import com.bradpark.utils.PjhUi
import kotlin.math.abs

class RefreshLayout(context: Context, attrs: AttributeSet? = null): ConstraintLayout(context, attrs),
    RefreshView {
    private var downX = 0f
    private var downY = 0f

    private var offsetY = 0f
    private var initLottieTopPosition = 0f // lottie 초기 위치 지정
    private var mDataBinding: RefreshModuleBinding? = null
    private var contentChildRecyclerView: RecyclerView? = null
    private var currentState  = State.IDLE

    private val onTriggerListeners: MutableCollection<() -> Unit> = mutableListOf()
    private lateinit var lottieView: LottieAnimationView
    private lateinit var topView: ConstraintLayout
    private lateinit var contentView: View

    init {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.refresh_module, this, true)
    }

    /**
     * Inflate after event
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        (0 until childCount).map {
            val child = getChildAt(it)
            if (child is ConstraintLayout) {
                (0 until child.childCount).map { index ->
                    val childTemp = child.getChildAt(index)
                    if (childTemp is RecyclerView) {
                        contentChildRecyclerView = childTemp
                    } else if (childTemp is LottieAnimationView) {
                        lottieView = childTemp
                    }
                }
            }
            when (child.contentDescription) {
                CONTENT -> contentView = child
            }
        }
        topView = mDataBinding?.clRefreshModuleParent!!
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        fun checkIfScrolledFurther(ev: MotionEvent, dy: Float, dx: Float): Boolean {
            val view = if (contentChildRecyclerView != null) {
                contentChildRecyclerView
            } else {
                contentView
            }
            return if (view?.canScrollVertically(-1) == false) {
                ev.y > downY && abs(dy) > abs(dx)
            } else {
                false
            }
        }

        var shouldStealTouchEvents = false
        if (currentState != State.IDLE) {
            shouldStealTouchEvents = true
        }
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - downX
                val dy = ev.y - downY
                if (abs(dx) > PREVENTION_MOVE || abs(dy) > PREVENTION_MOVE) {
                    shouldStealTouchEvents = checkIfScrolledFurther(ev, dy, dx)
                }
            }
        }
        return shouldStealTouchEvents
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handledTouchEvent = true
        if (currentState != State.IDLE) {
            return false
        }

        if (currentState != State.IDLE) {
            handledTouchEvent = false
        }
        parent.requestDisallowInterceptTouchEvent(true)
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                if (currentState == State.IDLE) {
                    offsetY = (event.y - downY) * (1 - STICKY_FACTOR * STICKY_MULTIPLIER)
                    move()
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                if (currentState == State.IDLE && triggerOffSetTop <= offsetY) {
                    startRefreshing()
                } else {
                    contentView.animate().translationY(0f).duration = contentViewRollBackDuration
                    lottieView.animate().translationY(initLottieTopPosition).duration = lottieViewRollBackDuration
                }
            }
        }

        return handledTouchEvent
    }

    private fun move() {
        offsetY = if (offsetY < 0) 0f else if (offsetY > maxOffSetTop) maxOffSetTop else offsetY
        contentView.y = offsetY
        if (lottieView.visibility != View.VISIBLE) {
            topView.visibility = View.VISIBLE
            lottieView.visibility = View.VISIBLE
            lottieView.cancelAnimation()
            lottieView.progress = 0f
        }
        /**
         * 근접치 alpha 값 제공
         * @return Float
         */
        fun getProximityAlpha(): Float {
            val centerValue = triggerOffSetTop / 2
            return if (offsetY < centerValue ) {
                0f
            } else if (offsetY > centerValue &&  offsetY < triggerOffSetTop) {
                val temp = centerValue / 5
                val tempOffset = (offsetY - centerValue)
                when {
                    tempOffset < temp -> {
                        0.2f
                    }
                    tempOffset < temp * 1.5 -> {
                        0.3f
                    }
                    tempOffset < temp * 2 -> {
                        0.4f
                    }
                    tempOffset < temp * 2.5 -> {
                        0.5f
                    }
                    tempOffset < temp * 3 -> {
                        0.6f
                    }
                    tempOffset < temp * 3.5 -> {
                        0.7f
                    }
                    tempOffset < temp * 4 -> {
                        0.8f
                    }
                    tempOffset < temp * 4.5 -> {
                        0.9f
                    }
                    else -> {
                        1.0f
                    }
                }
            } else {
                1f
            }
        }

        if (lottiePosition != CENTER) {
            lottieView.alpha = getProximityAlpha()
        }

        val calcPosition = initLottieTopPosition + (offsetY/ 2)
        when {
            lottieView.y < 0 -> {
                lottieView.y = calcPosition
            }
            offsetY >= triggerOffSetTop && lottiePosition != CENTER -> {
                lottieView.y = 0f
            }
            else -> {
                lottieView.y = calcPosition
            }
        }

    }

    /**
     * Refresh 가 시작
     *
     * @param smooth Boolean
     */
    override fun startRefreshing(smooth: Boolean) {
        offsetY = triggerOffSetTop + 0.1f
        currentState = State.ROLLING
        val rollBackOffset = if (offsetY > triggerOffSetTop) offsetY - triggerOffSetTop else offsetY
        val triggerOffset: Float = if (rollBackOffset != offsetY) triggerOffSetTop else 0F
        handler.post {
            contentChildRecyclerView?.let {
                if (smooth) {
                    it.smoothScrollToPosition(0)
                } else {
                    it.scrollToPosition(0)
                }
            }
        }
        lottieView.repeatCount = LottieDrawable.INFINITE
        lottieView.playAnimation()
        offsetY = triggerOffset
        currentState = State.TRIGGERING
        onTriggerListeners.forEach { it() }
        contentView.animate().translationY(triggerOffset).duration = contentViewRollBackDuration
        lottieView.animate().translationY(0f).duration = lottieViewRollBackDuration
        move()
    }

    /**
     * Refresh 가 종료
     *
     */
    override fun stopRefreshing() {
        contentView.animate().translationY(0f).duration = contentViewRollBackDuration
        lottieView.animate().translationY(initLottieTopPosition).duration = lottieViewRollBackDuration
        lottieView.cancelAnimation()
        lottieView.progress = 0f
        lottieView.y = initLottieTopPosition
        offsetY = 0f
        currentState = State.IDLE
    }


    /**
     * Refresh 여부
     * @return Boolean
     */
    override fun isRefreshing(): Boolean {
        return currentState != State.IDLE
    }

    fun onTriggerListener(onTriggerListener: () -> Unit) {
        onTriggerListeners.add(onTriggerListener)
    }

    fun removeOnTriggerListener(onTriggerListener: () -> Unit) {
        onTriggerListeners.remove(onTriggerListener)
    }

    enum class State {
        IDLE,
        ROLLING,
        TRIGGERING
    }

    companion object {
        private const val PREVENTION_MOVE = 15
        private const val STICKY_FACTOR = 0.66f
        private const val STICKY_MULTIPLIER = 0.75f
        private var contentViewRollBackDuration = 500L
        private var lottieViewRollBackDuration = 500L
        private const val CONTENT = "content"
        private const val CENTER = "center"
        var triggerOffSetTop:Float = 94f
        var maxOffSetTop:Float = 656f
        private var lottieFileName: String = "default_pull_loading.json"
        var lottiePosition: String = ""


        /**
         * refresh height 값 setting
         * @param view CubeRefreshLayout view
         * @param max Int unit : DP
         * @param trigger Int unit : DP
         */
        @BindingAdapter("refreshMax", "refreshTrigger")
        @JvmStatic
        fun getMaxTriggerData(view: RefreshLayout, max: Int, trigger: Int) {
            maxOffSetTop = PjhUi.convertDpToPixels(view.context, max.toFloat())
            triggerOffSetTop = PjhUi.convertDpToPixels(view.context, trigger.toFloat())
            view.initLottieTopPosition = (triggerOffSetTop / 2) * -1
            view.lottieView.y = view.initLottieTopPosition
            val params = view.lottieView.layoutParams as ViewGroup.LayoutParams
            params.height = triggerOffSetTop.toInt()
            view.lottieView.layoutParams = params
        }

        /**
         * Lottie 위치 setting
         * @param view CubeRefreshLayout
         * @param lottiePosition String : xml 에서 "center"로 지정 했을 경우 contentView 를 pull 한 만큼 lottieView 의 위치가 내려옴.
         */
        @BindingAdapter("lottiePosition")
        @JvmStatic
        fun getLottiePositionData(view: RefreshLayout, lottiePosition: String = "") {
            when (lottiePosition) {
                CENTER -> {
                    val params = view.topView.layoutParams as ViewGroup.LayoutParams
                    params.height = maxOffSetTop.toInt()

                    Companion.lottiePosition = lottiePosition
                }
            }
        }

        /**
         * contentView, lottieView 가 제자리로 돌아가는 속도
         * @param view CubeRefreshLayout
         * @param contentViewDuration Long
         * @param lottieViewDuration Long
         */
        @BindingAdapter("contentViewDuration", "lottieViewDuration")
        @JvmStatic
        fun setRollBackDuration(view: RefreshLayout, contentViewDuration: Long = 500, lottieViewDuration: Long = 500) {
            contentViewRollBackDuration = contentViewDuration
            lottieViewRollBackDuration = lottieViewDuration
        }

        /**
         * assert file 설정
         * 파일명 잘못 설정 시 기본 lottieFile 제공
         * @param view CubeRefreshLayout
         * @param fileName String "default_pull_loading.json" 파일명
         */
        @BindingAdapter("refreshLottieAssetFileName")
        @JvmStatic
        fun setPullToRefreshLottieAssetFileName(view: RefreshLayout, fileName: String) {
            try {
                lottieFileName = fileName
                view.lottieView.setAnimation(lottieFileName)
            } catch (e: Exception) {
                lottieFileName = "default_pull_loading.json"
                view.lottieView.setAnimation(lottieFileName)
            }
        }

        /**
         * Lottie Width 설정
         * @param view CubeRefreshLayout
         * @param width Int
         */
        @BindingAdapter("refreshLottieWidth")
        @JvmStatic
        fun setPullToRefreshLottieWidth(view: RefreshLayout, width: Int) {
            val params = view.lottieView.layoutParams as ViewGroup.LayoutParams
            params.width = PjhUi.convertDpToPixels(view.context, width.toFloat()).toInt()
            view.lottieView.layoutParams = params
        }
    }
}