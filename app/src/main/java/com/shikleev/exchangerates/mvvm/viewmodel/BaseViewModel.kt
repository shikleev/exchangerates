package com.shikleev.exchangerates.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shikleev.exchangerates.extensions.tryCatch
import com.shikleev.exchangerates.mvvm.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    fun <T> requestWithLiveData(
        liveData: MutableLiveData<Event<T>>,
        request: suspend () -> T
    ) {
        liveData.postValue(Event.loading())
        this.viewModelScope.launch(Dispatchers.IO) {
            tryCatch({
                val response = request.invoke()
                if (response != null) {
                    liveData.postValue(Event.success(response))
                } else if (response != null) {
                    errorWithLiveData(response, liveData) {
                        requestWithLiveData(liveData, request)
                    }
                }
            }, {
                liveData.postValue(Event.error(null))
            })
        }
    }

    fun <T> requestWithCallback(
        request: suspend () -> T,
        response: (Event<T>) -> Unit
    ) {
        response(Event.loading())
        this.viewModelScope.launch(Dispatchers.IO) {
            tryCatch({
                val res = request.invoke()
                launch(Dispatchers.Main) {
                    if (res != null) {
                        response(Event.success(res))
                    } else if (res != null) {
                        errorWithCallback<T>(res, response) {
                            requestWithCallback(request, response)
                        }
                    }
                }
            }, {
                launch(Dispatchers.Main) {
                    response(Event.error(null))
                }
            })
        }
    }

    private fun <T> errorWithLiveData(
        errorResponse: Error?,
        liveData: MutableLiveData<Event<T>>,
        request: () -> Unit
    ) {
        liveData.postValue(Event.error(errorResponse))
    }

    private fun <T> errorWithCallback(
        errorResponse: Error?,
        response: (Event<T>) -> Unit,
        request: () -> Unit
    ) {
        response(Event.error(errorResponse))
    }
}