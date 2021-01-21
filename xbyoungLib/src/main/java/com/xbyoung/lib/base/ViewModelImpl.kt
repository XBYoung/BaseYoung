package com.xbyoung.lib.base


import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KClass

interface ViewModelImpl {
    fun buildObservableClasses(types: List<KClass<*>>)

    /**
     * 直接将对象更新
     */
    fun postValue(data: Any)

    /**
     * 更具类型获取LiveData
     */
    fun loadLiveData(cls: KClass<*>): MutableLiveData<*>?
}