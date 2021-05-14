package com.bradpark.calllback

import android.view.View
import com.bradpark.config.PjhConfig
import com.bradpark.ui.bottomsheet.BottomSheetFragment

interface BottomSheetStateCallback {
    fun onResume(bottomSheet: BottomSheetFragment, type: PjhConfig.PjhBottomSheetType, containerView: View? = null, bottomView: View? = null)
}