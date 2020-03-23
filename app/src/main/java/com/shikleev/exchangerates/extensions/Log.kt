package com.shikleev.exchangerates.extensions

import android.util.Log
import com.shikleev.exchangerates.BuildConfig

fun logInfoExtended(text: Any? = "No message") {
    log(text = text.toString(), type = "i", extended = true)
}

fun logInfo(text: Any? = "No message") {
    log(text = text.toString(), type = "i", extended = false)
}

fun logErrorExtended(text: Any? = "No message") {
    log(text = text.toString(), type = "e", extended = true)
}

fun logError(text: Any? = "No message") {
    log(text = text.toString(), type = "e", extended = false)
}

fun logDebugExtended(text: Any? = "No message") {
    log(text = text.toString(), type = "d", extended = true)
}

fun logDebug(text: Any? = "No message") {
    log(text = text.toString(), type = "d", extended = false)
}


private fun log(text: String?, type: String, extended: Boolean) {
    if (BuildConfig.DEBUG) {
        val TAG = "LOGGER"
        tryCatch {
            val stackTrace = Thread.currentThread().stackTrace
            var path = stackTrace[4].toString()
            path = path.replace("${BuildConfig.APPLICATION_ID}.", "")
            var resultText: String? = null
            text?.let {
                val period = 150
                val builder = StringBuilder(
                        text.length + 2 * (text.length / period) + 1)
                var index = 0
                var prefix: String? = ""
                while (index < text.length) {
                    builder.append(prefix)
                    prefix = "\n"
                    builder.append(text.substring(index, (index + period).coerceAtMost(text.length)))
                    index += period
                }
                resultText = builder.toString()
            }
            if (extended) {
                val message = " \n\n---------- \n\n PATH:   ${path}\n\n MESSAGE:   $resultText \n\n ----------\n\n "
                when (type) {
                    "i" -> Log.i(TAG, message)
                    "e" -> Log.e(TAG, message)
                    "d" -> Log.d(TAG, message)
                }
            } else {
                val message = "${path} |-----| $resultText"
                when (type) {
                    "i" -> Log.i(TAG, message)
                    "e" -> Log.e(TAG, message)
                    "d" -> Log.d(TAG, message)
                }
            }
        }
    }
}