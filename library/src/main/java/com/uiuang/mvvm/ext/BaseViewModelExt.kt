package com.uiuang.mvvm.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.uiuang.mvvm.base.activity.BaseVmActivity
import com.uiuang.mvvm.base.activity.BaseVmDbActivity
import com.uiuang.mvvm.base.fragment.BaseVmFragment
import com.uiuang.mvvm.base.viewmodel.BaseViewModel
import com.uiuang.mvvm.network.AppException
import com.uiuang.mvvm.network.BaseResponse
import com.uiuang.mvvm.network.ExceptionHandle
import com.uiuang.mvvm.state.ResultState
import com.uiuang.mvvm.state.paresException
import com.uiuang.mvvm.state.paresResult
import com.uiuang.mvvm.util.loge
import kotlinx.coroutines.*
import java.io.File


/**
 * 作者　: hegaojian
 * 时间　: 2020/4/8
 * 描述　:BaseViewModel请求协程封装
 */

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmDbActivity<*, *>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmActivity<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmFragment<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge("Mvvm")
            resultState.paresException(it)
        }
    }
}

/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoCheck(
    block: suspend () -> T,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge("mvvm")
            resultState.paresException(it)
        }
    }
}

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    //如果需要弹窗 通知Activity/fragment弹窗
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            //网络请求成功 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            runCatching {
                //校验请求结果码是否正确，不正确会抛出异常走下面的onFailure
                executeResponse(it) { t -> success(t) }
            }.onFailure { e ->
                //打印错误消息
                e.message?.loge("mvvm1")
                //失败回调
                error(ExceptionHandle.handleException(e))
            }
        }.onFailure {
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            //打印错误消息
            it.message?.loge("mvvm2")
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

/**
 *  不过滤请求结果
 * @param block 请求体 必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不给
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoCheck(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
): Job {
    //如果需要弹窗 通知Activity/fragment弹窗
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    return viewModelScope.launch {
        runCatching {
            //请求体
            block()
        }.onSuccess {
            //网络请求成功 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            //成功回调
            success(it)
        }.onFailure {
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            //打印错误消息
            it.message?.loge("mvvm")
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

fun <T> BaseViewModel.saveImageToGallery(context: Context,mImageUrl: String, mImageTitle: String,
                                         success: (String) -> Unit,
                                         error: (String) -> Unit = {},
                                         isShowDialog: Boolean = false,
                                         loadingMessage: String = "请求网络中..."
): Job {
    if (isShowDialog) loadingChange.showDialog.postValue(loadingMessage)
    return viewModelScope.launch {
        runCatching {
            // 检查路径
            if (mImageUrl.isEmpty() || mImageTitle.isEmpty()) {
               error("请检查图片路径")
            }
            // 检查图片是否已存在
            // 检查图片是否已存在
            val appDir = File(context.filesDir, "云阅相册")
            if (appDir.exists()) {
                val file = File(appDir, getFileName(mImageUrl, mImageTitle)
                )
                if (file.exists()) {
                    error("图片已存在")
                }
            }
            // 没有目录创建目录
            // 没有目录创建目录
            if (!appDir.exists()) {
                appDir.mkdir()
            }

        }.onSuccess {
            //网络请求成功 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            //成功回调
            success("")
        }.onFailure {
            //网络请求异常 关闭弹窗
            loadingChange.dismissDialog.postValue(false)
            //打印错误消息
            it.message?.loge("mvvm")
            //失败回调
            error("")
        }
    }
}

/**
 * gif动态图以对应后缀结尾
 */
fun getFileName(mImageUrl: String, mImageTitle: String): String {
    return if (mImageUrl.contains(".gif")) {
        mImageTitle.replace("/", "-") + ".gif"
    } else {
        mImageTitle.replace("/", "-") + ".jpg"
    }
}


/**
 * 请求结果过滤，判断请求服务器请求结果是否成功，不成功则会抛出异常
 */
suspend fun <T> executeResponse(
    response: BaseResponse<T>,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        if (response.isSuccess()) success(response.getResponseData())
        else throw AppException(
            response.getResponseCode(),
            response.getResponseMsg(),
            response.getResponseMsg()
        )
    }
}

/**
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> BaseViewModel.launch(
    block: () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}
