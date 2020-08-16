package com.parassidhu.coronavirusapp.di

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.parassidhu.coronavirusapp.CoronaApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule() {

    @Provides
    @Singleton
    fun provideApp(): Application {
        return CoronaApp.instance
    }

    @Provides
    @Singleton
    fun getContext(): Context = CoronaApp.instance

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}