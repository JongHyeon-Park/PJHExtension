package com.bradpark.component

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bradpark.config.PjhConfig
import com.bradpark.ui.snackbar.PjhSnackBar

/**
 * BaseActivity
 * ViewDataBinding 쉽게 사용할 수 있는 OpenClass
 * @param T
 */
open class BaseActivity<T : ViewDataBinding>: AppCompatActivity() {
    private var mDataBinding: T? = null
    private var cubeSnackBar: PjhSnackBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSetting()
    }

    private fun initSetting() {
        this@BaseActivity.supportActionBar?.setDisplayShowCustomEnabled(true)
        this@BaseActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        cubeSnackBar = null
        mDataBinding?.unbind()
    }

    protected fun setDataBinding(@LayoutRes layoutRes: Int) {
        this.mDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    }

    protected fun getDataBinding(): T? {
        return mDataBinding
    }

    protected fun showCubeSnackBarMessage(type: PjhConfig.PjhSnackBarType,
                                          message: String,
                                          iconId: Int,
                                          positionType: PjhConfig.PjhSnackPositionType = PjhConfig.PjhSnackPositionType.NORMAL,
                                          navigationBarStatus: PjhConfig.PjhSnackNavigationBarStatus = PjhConfig.PjhSnackNavigationBarStatus.No) {
        mDataBinding?.let {
            cubeSnackBar = PjhSnackBar.make(view = it.root, mode = type,message = message,iconId = iconId,type = positionType, navigationType = navigationBarStatus)
            cubeSnackBar?.show()
        }
    }

    protected fun getCubeSnackBar() = cubeSnackBar
}