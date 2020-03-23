package com.shikleev.exchangerates.di

import com.shikleev.exchangerates.App
import com.shikleev.exchangerates.mvvm.viewmodel.RatesViewModel
import com.shikleev.exchangerates.view.fragment.BaseFragment
import com.shikleev.exchangerates.view.fragment.ConverterFragment
import com.shikleev.exchangerates.view.fragment.RatesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    NetworkModule::class])
interface ApplicationComponent {
    fun inject(target: App)
    fun inject(target: RatesViewModel)
    fun inject(target: BaseFragment)
    fun inject(target: RatesFragment)
    fun inject(target: ConverterFragment)
}