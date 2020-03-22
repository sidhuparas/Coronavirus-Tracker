package com.parassidhu.coronavirusapp.di

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.parassidhu.coronavirusapp.CoronaApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(var app: CoronaApp) {

    @Provides
    @Singleton
    fun provideApp(): Application {
        return app
    }

    @Provides
    @Singleton
    fun getContext(): Context = app

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}