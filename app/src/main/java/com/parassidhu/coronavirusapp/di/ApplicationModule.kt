package com.parassidhu.coronavirusapp.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.parassidhu.coronavirusapp.CoronaApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule() {

    @Provides
    @Singleton
    fun getContext(): Context = CoronaApp.instance

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}