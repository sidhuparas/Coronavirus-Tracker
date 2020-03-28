package com.parassidhu.coronavirusapp.util

import android.os.Handler
import android.util.Log
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.parassidhu.coronavirusapp.CoronaApp

fun cornerRadius(value: Int): RequestOptions {
    return RequestOptions().transform(
        CenterCrop(),
        RoundedCorners(
            dp2px(value.toFloat())
        )
    )
}

fun dp2px(dp: Float): Int {
    val context = CoronaApp.instance
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

suspend fun <T> safeApiCall(
    call: suspend () -> T,
    onSuccess: (NetworkResult.Success<T>) -> Unit,
    onFailure: (NetworkResult.Error) -> Unit
) {
    runCatching {
        val response = call()
        onSuccess.invoke(NetworkResult.Success(response))
    }.onFailure {
        it.printStackTrace()
        onFailure.invoke(NetworkResult.Error(it.message))
    }
}

fun runInHandler(timeInMillis: Long, action: () -> Unit) {
    Handler().postDelayed(action, timeInMillis)
}

fun log(message: String?) {
    Log.d("Paras", message)
}