package com.xbyoung.lib.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import com.xbyoung.lib.R
import kotlinx.android.synthetic.main.dialog_loading.*


class LoadingDialog(context: Context) : Dialog(context, R.style.LoadingDialog) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        setContentView(view)
        this.setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setOnKeyListener { _, _, event ->
            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                if (context is Activity) {
                    context.finish()
                }
                true
            }
            false
        }
    }

    override fun show() {
        super.show()
        this.avi.smoothToShow()
    }

    override fun dismiss() {
        this.avi.smoothToHide()
        super.dismiss()
    }

}