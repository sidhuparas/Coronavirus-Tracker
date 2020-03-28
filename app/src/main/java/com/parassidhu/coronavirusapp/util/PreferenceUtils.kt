package com.parassidhu.coronavirusapp.util

import androidx.preference.PreferenceManager
import com.parassidhu.coronavirusapp.CoronaApp

object PreferenceUtils {
    val pref = PreferenceManager.getDefaultSharedPreferences(CoronaApp.instance)
}