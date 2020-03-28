package com.parassidhu.coronavirusapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parassidhu.coronavirusapp.network.response.CountryStat

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CountryStat>)

    @Query("SELECT * FROM country_table")
    suspend fun getCountries(): List<CountryStat>
}