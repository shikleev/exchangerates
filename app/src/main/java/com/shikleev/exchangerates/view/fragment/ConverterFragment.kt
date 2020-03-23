package com.shikleev.exchangerates.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shikleev.exchangerates.App
import com.shikleev.exchangerates.R
import com.shikleev.exchangerates.data.datastore.DataStore
import com.shikleev.exchangerates.data.model.Rate
import com.shikleev.exchangerates.extensions.toast
import com.shikleev.exchangerates.mvvm.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.android.synthetic.main.fragment_rates.*
import javax.inject.Inject

class ConverterFragment : BaseFragment() {

    private lateinit var ratesViewModel: RatesViewModel
    private var currentRate: Rate? = null

    @Inject
    lateinit var dataStore: DataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratesViewModel = ViewModelProvider(this).get(RatesViewModel::class.java)
        App.component.inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRatesObserver()
        setInputListener()
    }

    private fun setRatesObserver() {
        dataStore.ratesDao().getRates().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                ratesViewModel.getRates("RUB")
            } else {
                currentRate = it
            }
        })
    }

    private fun setInputListener() {
        et_input.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateRate(p0.toString())
            }
        })
    }

    private fun calculateRate(input: String?) {
        input?.let {
            val rates = currentRate?.rates
            if (it.toIntOrNull() != null
                && rates?.usd != null
                && rates.eur != null) {
                tv_usd_rate.text = String.format("%.2f", (it.toInt() * rates.usd))
                tv_eur_rate.text = String.format("%.2f", (it.toInt() * rates.eur))
            } else if (it.isEmpty()) {
                tv_usd_rate.text = 0.toString()
                tv_eur_rate.text = 0.toString()
            } else {
                requireContext().toast(R.string.error)
            }
        } ?: run {
            tv_usd_rate.text = 0.toString()
            tv_eur_rate.text = 0.toString()
        }
    }
}
