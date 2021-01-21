package com.xbyoung.lib.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
Young
 */
abstract class BaseAdapter<T>(open val context: Context, open val click: ((T) -> Unit)? = {}) : RecyclerView.Adapter<BaseAdapterHolder<T>>() {
    var datas = mutableListOf<T>()
    val infalte by lazy {
        LayoutInflater.from(context)
    }

    open fun initData(data: MutableList<T>) {
        this.datas = data
        this.notifyDataSetChanged()
    }

    open fun addDatasToEnd(data: MutableList<T>) {
        this.datas.addAll(data)
        this.notifyDataSetChanged()
    }

    open fun addData(data: T) {
        this.datas.add(0, data)
        this.notifyDataSetChanged()
    }

    abstract fun getLayoutId(): Int
    override fun onCreateViewHolder(group: ViewGroup, postion: Int): BaseAdapterHolder<T> {
        return BaseHolder(infalte.inflate(getLayoutId(), group, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: BaseAdapterHolder<T>, postion: Int) {
        holder.bind(datas[postion], postion)
    }

    abstract fun bind(bean: T, position: Int, item: View)

    inner class BaseHolder(item: View) : BaseAdapterHolder<T>(item) {
        override fun bind(bean: T, position: Int) {
            item.setOnClickListener {
                click?.invoke(bean)
            }
            bind(bean, position, item)
        }
    }
}