package com.parassidhu.coronavirusapp.di

import com.parassidhu.coronavirusapp.CoronaApp
import com.parassidhu.coronavirusapp.base.BaseActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(app: CoronaApp)
    fun inject(activity: BaseActivity)
}