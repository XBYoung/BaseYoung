package com.xbyoung.lib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class LazyFragment : Fragment() {

    private var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(context).inflate(contentViewId, null)
        initView(view)
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = true
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            initData()
            initEvent()
            isFirstLoad = false
        }
    }

    /**
     * 设置布局资源id
     *
     * @return
     */
    protected abstract val contentViewId: Int

    /**
     * 初始化视图
     *
     * @param view
     */
    open fun initView(view: View?) {}

    /**
     * 初始化数据
     */
    open fun initData() {}

    /**
     * 初始化事件
     */
    open fun initEvent() {}
}
