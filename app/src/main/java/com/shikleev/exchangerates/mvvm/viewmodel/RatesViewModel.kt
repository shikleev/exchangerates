package com.shikleev.exchangerates.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.shikleev.exchangerates.App
import com.shikleev.exchangerates.data.datastore.DataStore
import com.shikleev.exchangerates.data.model.Rate
import com.shikleev.exchangerates.data.network.MainApi
import com.shikleev.exchangerates.extensions.doAsync
import com.shikleev.exchangerates.extensions.logInfo
import com.shikleev.exchangerates.mvvm.Event
import com.shikleev.exchangerates.mvvm.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesViewModel : BaseViewModel() {

    @Inject
    lateinit var api: MainApi

    @Inject
    lateinit var dataStore: DataStore

    val latestRatesLiveData: LiveData<Event<Rate?>> = MutableLiveData<Event<Rate?>>()

    init {
        App.component.inject(this)
    }

    fun getRates(base: String) {
        (latestRatesLiveData as MutableLiveData).value = Event.loading()
        requestWithCallback({
            api.getLatest(base,"EUR,USD")
        }, {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 ->
                        latestRatesLiveData.value = Event.success(it1)
                        viewModelScope.launch(Dispatchers.Default) {
                            dataStore.ratesDao().insert(it1)
                        }
                    } ?: run {
                        (latestRatesLiveData as MutableLiveData).value = Event.error(null)
                    }
                }
                Status.ERROR -> {
                    latestRatesLiveData.value = Event.error(null)
                }
            }

        })
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.Default) {
            dataStore.ratesDao().deleteAll()
        }
    }

}