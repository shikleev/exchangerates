package com.shikleev.exchangerates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.shikleev.exchangerates.extensions.hideKeyboard
import com.shikleev.exchangerates.view.fragment.BaseFragment
import com.shikleev.exchangerates.view.fragment.ConverterFragment
import com.shikleev.exchangerates.view.fragment.RatesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var fragmentManager: FragmentManager? = null
    private var ratesFragment: BaseFragment? = null
    private var converterFragment: BaseFragment? = null
    private var currentFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        bottomNavigationListener()
        selectRatesFragment()
    }

    private fun bottomNavigationListener() {
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_rates -> {
                    selectRatesFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_converter -> {
                    selectConverterFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun selectRatesFragment() {
        if (ratesFragment != null && currentFragment == ratesFragment) {
//            (ratesFragment as RatesFragment).scrollToUp()
            return
        }
        if (ratesFragment == null)
            ratesFragment = RatesFragment()
        commitFragmentTransaction(ratesFragment!!)
    }

    private fun selectConverterFragment() {
        if (converterFragment != null && currentFragment == converterFragment) {
//            (converterFragment as ConverterFragment).scrollToUp()
            return
        }
        if (converterFragment == null)
            converterFragment = ConverterFragment()
        commitFragmentTransaction(converterFragment!!)
    }

    private fun commitFragmentTransaction(fragment: BaseFragment) {
        if (fragment == currentFragment)
            return

        val transaction = fragmentManager!!.beginTransaction()
        if (currentFragment != null) {
            transaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .hide(currentFragment!!)
        }
        if (!fragment.isAdded) {
            transaction.add(R.id.container, fragment)
        }
        transaction
            .attach(fragment)
            .show(fragment)
            .commitNow()
        currentFragment = fragment

        window.decorView.hideKeyboard()
    }

}
