package com.parassidhu.coronavirusapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.FavoriteCountry

@Database(entities = [CountryStat::class, FavoriteCountry::class], version = 1)
abstract class CoronaDatabase: RoomDatabase() {

    abstract fun countryDao(): CountryDao
}