package com.saynope.drawandguess.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saynope.drawandguess.repo.ApiResponse
import com.saynope.drawandguess.repo.EmptyBody
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val loadingState: MutableLiveData<Boolean> = MutableLiveData(false)
    val requestResultState: MutableLiveData<ApiResponse<EmptyBody>> = MutableLiveData()

    private fun handleRequestFail(code: Int, message: String) {
        requestResultState.value = ApiResponse.buildResponse(code, message)
    }

    protected fun <T> safeCallNet(
        block: suspend () -> ApiResponse<T>,
        realSuccessBlock: (t: T) -> Unit
    ) {
        loadingState.postValue(true)
        viewModelScope.launch {
            kotlin.runCatching {
                block.invoke()
            }.onSuccess { apiResponse ->
                loadingState.postValue(false)
                if (apiResponse.code == ApiResponse.RESULT_SUCCESS) {
                    realSuccessBlock.invoke(apiResponse.t)
                } else {
                    handleRequestFail(
                        apiResponse.code,
                        if (apiResponse.message.isEmpty()) "请求错误" else apiResponse.message
                    )
                }
            }.onFailure { e ->
                loadingState.postValue(false)
                handleRequestFail(800, e.message ?: "")
            }
        }
    }

}