package com.shikleev.exchangerates.view.fragment

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.shikleev.exchangerates.App
import com.shikleev.exchangerates.MainActivity
import com.shikleev.exchangerates.extensions.logError
import com.shikleev.exchangerates.extensions.logInfo
import com.shikleev.exchangerates.extensions.simpleName
import java.io.Serializable


abstract class BaseFragment : Fragment(), Serializable {

    lateinit var act: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
        App.component.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logError("ON_VIEW_CREATED: ${javaClass.simpleName}")
    }

    override fun onDestroyView() {
        logInfo("ON_DESTROY_VIEW: $simpleName")
        super.onDestroyView()
    }

    override fun onDestroy() {
        logInfo("ON_DESTROY: $simpleName")
        super.onDestroy()
    }
}