package com.parassidhu.coronavirusapp.di

import androidx.room.Room
import com.parassidhu.coronavirusapp.CoronaApp
import com.parassidhu.coronavirusapp.db.CoronaDatabase
import com.parassidhu.coronavirusapp.db.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule() {


    @Singleton
    @Provides
    fun getApp(): CoronaApp {
        return CoronaApp.instance
    }

    @Singleton
    @Provides
    fun getDatabase(): CoronaDatabase {
        return Room.databaseBuilder(CoronaApp.instance, CoronaDatabase::class.java, "corona_database")
            .build()
    }

    @Singleton
    @Provides
    fun getCoronaDao(db: CoronaDatabase): CountryDao {
        return db.countryDao()
    }
}