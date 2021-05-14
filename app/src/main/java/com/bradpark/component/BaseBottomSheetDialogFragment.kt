package com.bradpark.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * BottomSheetDialogFragment
 * ViewDataBinding 쉽게 사용할 수 있는 OpenClass
 * @param T
 */
open class BaseBottomSheetDialogFragment<T : ViewDataBinding>: BottomSheetDialogFragment() {

    private var mDataBinding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    protected fun setBinding(inflater: LayoutInflater, @LayoutRes layoutRes: Int, container: ViewGroup?): View {
        this.mDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return mDataBinding!!.root
    }

    protected fun getBinding(): T? {
        return this.mDataBinding
    }
}