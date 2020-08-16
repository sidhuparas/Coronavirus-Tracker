package com.parassidhu.coronavirusapp

import android.app.Application
import com.facebook.stetho.Stetho
import com.parassidhu.coronavirusapp.di.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoronaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        initRest()
    }

    private fun initRest() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    companion object {
        lateinit var instance: CoronaApp
            private set
    }
}