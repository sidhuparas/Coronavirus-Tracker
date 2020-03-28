package com.parassidhu.coronavirusapp.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun ShimmerFrameLayout.start() {
    isVisible = true
    startShimmer()
}

fun ShimmerFrameLayout.stop() {
    isVisible = false
    stopShimmer()
}

fun View.setWeight(weight: Float) {
    val param = LinearLayout.LayoutParams(
        layoutParams.width,
        layoutParams.height,
        weight
    )

    param.marginEnd = dp2px(5f)

    layoutParams = param
}

fun Fragment.showKeyboard(view: View, flag: Boolean) = requireContext().showKeyboard(view, flag)

fun Context.showKeyboard(view: View, flag: Boolean) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    if (flag)
        inputMethodManager.showSoftInput(view, 0)
    else
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) = requireContext().toast(message)

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)