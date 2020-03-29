package com.parassidhu.coronavirusapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.FavoriteCountry

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CountryStat>)

    @Query("SELECT * FROM country_table")
    fun getCountries(): LiveData<List<CountryStat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(item: FavoriteCountry)

    @Query("SELECT COUNT(*) FROM fav_country_table WHERE countryName = :name")
    suspend fun checkFavorite(name: String): Int

    @Delete
    suspend fun removeFromFavorite(item: FavoriteCountry)

    @Query("SELECT * FROM fav_country_table")
    fun getAllFavorites(): LiveData<List<FavoriteCountry>>
}