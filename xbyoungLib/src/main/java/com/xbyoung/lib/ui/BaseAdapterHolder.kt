package com.xbyoung.lib.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
Young
2019/1/16 8:50
 */
abstract class  BaseAdapterHolder <T> (val item: View) : RecyclerView.ViewHolder(item)  {
    abstract fun bind(bean:T,position:Int)
}