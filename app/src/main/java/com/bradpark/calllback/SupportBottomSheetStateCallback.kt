package com.bradpark.calllback

import android.view.View
import com.bradpark.config.PjhConfig
import com.bradpark.ui.bottomsheet.SupportBottomSheetFragment

interface SupportBottomSheetStateCallback {
    fun onResume(bottomSheet: SupportBottomSheetFragment, type: PjhConfig.PjhBottomSheetType, containerView: View? = null, bottomView: View? = null)
}