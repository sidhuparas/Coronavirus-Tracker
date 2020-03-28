package com.parassidhu.coronavirusapp

import android.app.Application
import com.facebook.stetho.Stetho
import com.parassidhu.coronavirusapp.di.*

class CoronaApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        initDagger()
        initRest()
    }

    private fun initRest() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    private fun initDagger() {
        component =
            DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .databaseModule(DatabaseModule(this))
                .build()

        component.inject(this)
    }

    companion object {
        lateinit var instance: CoronaApp
            private set
        lateinit var component: ApplicationComponent
            private set
    }
}