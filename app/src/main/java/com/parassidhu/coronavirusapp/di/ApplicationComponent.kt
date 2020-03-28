package com.parassidhu.coronavirusapp.di

import com.parassidhu.coronavirusapp.CoronaApp
import com.parassidhu.coronavirusapp.base.BaseActivity
import com.parassidhu.coronavirusapp.base.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class,
    ViewModelModule::class, DatabaseModule::class])
interface ApplicationComponent {

    fun inject(app: CoronaApp)
    fun inject(activity: BaseActivity)
    fun inject(baseFragment: BaseFragment)
}