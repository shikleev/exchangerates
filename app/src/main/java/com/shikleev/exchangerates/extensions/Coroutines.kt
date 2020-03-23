package com.shikleev.exchangerates.extensions

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


/**
 * Execute task on a fragment or activity on UI Thread with delay
 */
inline fun LifecycleOwner.doDelayed(delayMillis: Long, crossinline executeUi: () -> Unit) {
    this.lifecycleScope.launch(Dispatchers.Main) {
        delay(delayMillis)
        tryCatch {
            if (isActive)
                executeUi.invoke()
        }
    }
}

fun View.doDelayed(delayMillis: Long, executeUi: () -> Unit) {
    val job = CoroutineScope(Dispatchers.Main)
    val attachListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(p0: View?) {}
        override fun onViewDetachedFromWindow(p0: View?) {
            job.cancel()
            removeOnAttachStateChangeListener(this)
        }
    }
    this.addOnAttachStateChangeListener(attachListener)
    job.launch {
        delay(delayMillis)
        this@doDelayed.removeOnAttachStateChangeListener(attachListener)
        if (isActive) {
            tryCatch {
                executeUi.invoke()
            }
        }
    }
}

inline fun Any.doDelayed(delayMillis: Long, crossinline executeUi: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(delayMillis)
        tryCatch {
            if (isActive)
                executeUi.invoke()
        }
    }
}


inline fun <T> LifecycleOwner.doAsync(crossinline backgroundTask: (scope: CoroutineScope) -> T, crossinline result: (T?) -> Unit) {
    this.lifecycleScope.launch(Dispatchers.Main) {
        tryCatch {
            val task = withContext(Dispatchers.IO) {
                tryCatch({
                    backgroundTask(this)
                }, {
                    return@withContext null
                })
            }
            if (isActive)
                result.invoke(task)
        }
    }
}

inline fun <T> View.doAsync(
        crossinline backgroundTask: (scope: CoroutineScope) -> T,
        crossinline result: (T?) -> Unit) {
    val job = CoroutineScope(Dispatchers.Main)
    val attachListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(p0: View?) {}
        override fun onViewDetachedFromWindow(p0: View?) {
            job.cancel()
            removeOnAttachStateChangeListener(this)
        }
    }
    this.addOnAttachStateChangeListener(attachListener)
    job.launch {
        val data = async(Dispatchers.Default) {
            tryCatch({
                backgroundTask(this)
            }, {
                return@async null
            })
        }
        if (isActive) {
            tryCatch {
                result.invoke(data.await())
            }
        }
        this@doAsync.removeOnAttachStateChangeListener(attachListener)
    }
}

/**
 * Execute async on a background thread and get result UI Thread without lifecycle
 */
inline fun <T> Any.doAsync(crossinline backgroundTask: (scope: CoroutineScope) -> T, crossinline result: (T?) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        val task = async(Dispatchers.IO) {
            tryCatch({
                backgroundTask(this)
            }, {
                return@async null
            })
        }
        tryCatch {
            result.invoke(task.await())
        }
    }
}

/**
 * Execute async on a background thread and get result UI Thread
 */
inline fun <T> ViewModel.doAsyncViewModel(crossinline backgroundTask: (scope: CoroutineScope) -> T, crossinline result: (T?) -> Unit) {
    this.viewModelScope.launch(Dispatchers.Main) {
        val task = withContext(Dispatchers.IO) {
            tryCatch({
                backgroundTask(this@launch)
            }, {
                return@withContext null
            })
        }
        tryCatch {
            result.invoke(task)
        }
    }
}

