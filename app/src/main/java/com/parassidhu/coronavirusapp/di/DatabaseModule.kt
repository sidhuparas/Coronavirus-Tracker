package com.parassidhu.coronavirusapp.di

import androidx.room.Room
import com.parassidhu.coronavirusapp.CoronaApp
import com.parassidhu.coronavirusapp.db.CoronaDatabase
import com.parassidhu.coronavirusapp.db.CountryDao
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DatabaseModule(var application: CoronaApp) {

    private var database =
        Room.databaseBuilder(application, CoronaDatabase::class.java, "corona_database").build()

    @Singleton
    @Provides
    fun getApp(): CoronaApp {
        return application
    }

    @Singleton
    @Provides
    fun getDatabase(): CoronaDatabase {
        return database
    }

    @Singleton
    @Provides
    fun getCoronaDao(db: CoronaDatabase): CountryDao {
        return db.countryDao()
    }
}