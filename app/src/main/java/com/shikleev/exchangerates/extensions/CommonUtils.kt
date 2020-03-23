package com.shikleev.exchangerates.extensions

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader


fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

val <T : Any> T.simpleName
    get() = this::class.simpleName

inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(map: Map<String, Any>) =
        this.fromJson<T>(this.toJson(map), object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(reader: JsonReader) =
        this.fromJson<T>(reader, object : TypeToken<T>() {}.type)


val Int.dp: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Int.px: Int
    get() = ((this / Resources.getSystem().displayMetrics.density) + 0.5f).toInt()

val Float.px: Float
    get() = (this / Resources.getSystem().displayMetrics.density) + 0.5f

fun Int.isOdd() = this and 0x1 == 1

fun Int.isEven() = !this.isOdd()

val Long.trimLongNumber: String
    get() {
        return when {
            this > 1000000000 -> {
                val result = this.toFloat() / 1000000000
                String.format("%.1f", result) + "B"
            }
            this > 1000000 -> {
                val result = this.toFloat() / 1000000
                String.format("%.1f", result) + "M"
            }
            this > 1000 -> {
                val result = this.toFloat() / 1000
                String.format("%.1f", result) + "K"
            }
            else -> "$this"
        }
    }

val Int.trimLongNumber: String
    get() = this.toLong().trimLongNumber

inline fun ifNotNull(vararg values: Any?, crossinline block: () -> Unit): Unit? {
    values.forEach {
        if (it == null) return null
    }
    return block.invoke()
}

inline fun tryCatch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        logError("TRY_CATCH ________________: ${e.simpleName}: ${e.message}")
        e.stackTrace.forEach {
            logError("TRY_CATCH: $it")
        }
    }
}

inline fun <T> tryCatch(blockTry: () -> T, blockCatch: () -> T): T {
    return try {
        blockTry.invoke()
    } catch (e: Exception) {
        logError("TRY_CATCH ________________: ${e.simpleName}: ${e.message}")
        e.stackTrace.forEach {
            logError("TRY_CATCH: $it")
        }
        blockCatch()
    }
}


inline fun handlerLoop(interval: Long, crossinline block: () -> Unit): Handler {
    val handler = Handler()
    handler.post(object : Runnable {
        override fun run() {
            tryCatch {
                block()
            }
            handler.postDelayed(this, interval)
        }
    })
    return handler
}

fun Handler.stopLoop() {
    removeCallbacksAndMessages(null)
}


