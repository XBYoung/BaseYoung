package com.xbyoung.lib.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbyoung.lib.manager.ActivityManager
import com.xbyoung.lib.window.Eyes

abstract class BaseActivity : AppCompatActivity() {
    private val loadingDialog by lazy {
        newLoadingDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Eyes.transparencyBar(this)
        Eyes.setLightStatusBar(this, true)
        setContentView(contentViewId())
        ActivityManager.add(this)
        init()
    }


    abstract fun contentViewId(): Int

    open fun init() {}

    override fun finish() {
        hideLoading()
        super.finish()
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
        ActivityManager.remove(this)
    }


    open fun newLoadingDialog(): Dialog {
        return LoadingDialog(this)
    }

    fun showLoading() {
        if (!this.isFinishing && !this.isDestroyed && !loadingDialog?.isShowing) {
            loadingDialog?.show()
        }
    }


    fun hideLoading() {
        if (!this.isFinishing && !this.isDestroyed) {
            loadingDialog?.dismiss()
        }
    }
}