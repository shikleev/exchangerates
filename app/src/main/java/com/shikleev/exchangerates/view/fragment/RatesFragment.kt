package com.shikleev.exchangerates.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shikleev.exchangerates.App
import com.shikleev.exchangerates.R
import com.shikleev.exchangerates.data.datastore.DataStore
import com.shikleev.exchangerates.data.model.Rate
import com.shikleev.exchangerates.extensions.*
import com.shikleev.exchangerates.mvvm.Status
import com.shikleev.exchangerates.mvvm.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.fragment_rates.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesFragment : BaseFragment() {

    private lateinit var ratesViewModel: RatesViewModel

    @Inject
    lateinit var dataStore: DataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratesViewModel = ViewModelProvider(this).get(RatesViewModel::class.java)
        App.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        srl_rates.isRefreshing = true
        setLiveData()
        setRatesObserver()
        setSwipeRefreshLayout()
    }

    private fun setRatesObserver() {
        dataStore.ratesDao().getRates().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                ratesViewModel.getRates("RUB")
            } else {
                setTvRates(it)
            }
            srl_rates.isRefreshing = false
        })
    }

    private fun setTvRates(rate: Rate) {
        mcv_rates.visible()
        rate.rates?.usd?.let {
            tv_usd_result.text = String.format("%.2f", (1 / it))
        }
        rate.rates?.eur?.let {
            tv_eur_result.text = String.format("%.2f", (1 / it))
        }
    }

    private fun setSwipeRefreshLayout() {
        srl_rates.setOnRefreshListener {
            if (requireContext().isInternetConnected())
                ratesViewModel.deleteAll()
            else
                requireContext().toast(R.string.no_internet)
            srl_rates.isRefreshing = false
        }
    }

    private fun setLiveData() {
        ratesViewModel.latestRatesLiveData.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    srl_rates.isRefreshing = false
                    mcv_rates.visible()
                }
                Status.LOADING -> {
                    srl_rates.isRefreshing = true
                }
                Status.ERROR -> {
                    srl_rates.isRefreshing = false
                    mcv_rates.gone()
                    requireContext().toast(R.string.error)
                }
            }
        })
    }
}
