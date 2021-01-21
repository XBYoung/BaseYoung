package com.xbyoung.lib.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xbyoung.lib.XBYoungLib
import com.xbyoung.lib.model.ErrorResult
import com.xbyoung.lib.model.ResponseTag
import com.xbyoung.lib.utils.LogUtils
import com.xbyoung.lib.retrofit.helper.ErrorHelper
import kotlinx.coroutines.*
import kotlin.reflect.KClass

abstract class BaseViewModel : ViewModel(), ViewModelImpl, CoroutineScope by MainScope() {



    /**
     * 类型池
     */
    private val valuePool = mutableMapOf<KClass<*>, MutableLiveData<Any>>()

    override fun buildObservableClasses(types: List<KClass<*>>) {
        types.forEach {
            valuePool[it] = MutableLiveData<Any>()
        }
    }


    /**
     * 直接将对象更新
     */
    override fun postValue(data: Any) {
        val liveData = valuePool[data::class]
        liveData?.postValue(data)
    }

    /**
     * 类型获取LiveData
     */
    override fun loadLiveData(cls: KClass<*>): MutableLiveData<*>? {
        return valuePool[cls]
    }


    /**
     * 协程任务
     */
    fun <T : Any> doCoroutineWork(error: ((Throwable) -> Unit)? = null, work: suspend () -> T) {

        launch {
            runCatching {
                val result = work()
                if (result !is Unit?) {
                    postValue(result)
                }
            }.onFailure {
                error?.invoke(it)
                clearProcessBindNetWork()
                LogUtils.e("http  catch  inner  ${it.message}")
                val error = ErrorHelper.fromThrowable(it)
                LogUtils.e("http  $error")
                postValue(error)

            }
            postValue(ResponseTag())
            LogUtils.e("http  end")

        }
    }


    private fun clearProcessBindNetWork() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val connectivityManager =
                    XBYoungLib.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager?.bindProcessToNetwork(null)
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ConnectivityManager.setProcessDefaultNetwork(null)
                return
            }
        } catch (e: Throwable) {

        }

    }

    /**
     * 协程任务 复杂情况(需要viewModel自己去处理数据分发)
     */
    fun <T : Any> doCoroutineWorkHard(
        work: suspend () -> T,
        success: (response: T) -> Unit,
        fail: (error: ErrorResult) -> Unit
    ) {
        launch {
            runCatching {
                success(work())
            }.onFailure {
                LogUtils.e("http  catch  inner  ${it.message}")
                fail(ErrorHelper.fromThrowable(it))
            }
            postValue(ResponseTag())
        }
    }

    /**
     * 协程任务 多任务情况(需要viewModel自己去处理数据分发)
     */
    fun <T : Any, K : Any> doCoroutineWorkMutable(
        workT: suspend () -> T,
        workK: suspend () -> K,
        success: (responseT: T, responseK: K) -> Unit,
        fail: (error: ErrorResult) -> Unit
    ) {
        launch {
            runCatching {
                success(workT(), workK())
            }.onFailure {
                LogUtils.e("http  catch  inner  ${it.message}")
                fail(ErrorHelper.fromThrowable(it))
            }
            postValue(ResponseTag())
        }
    }

    fun onDestroy() {
        cancel()
    }
}
