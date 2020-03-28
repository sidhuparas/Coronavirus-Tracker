package com.parassidhu.coronavirusapp.ui.main

import androidx.lifecycle.LiveData
import com.parassidhu.coronavirusapp.db.CountryDao
import com.parassidhu.coronavirusapp.network.ApiClient
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.CountryWiseCase
import com.parassidhu.coronavirusapp.network.response.FavoriteCountry
import com.parassidhu.coronavirusapp.network.response.WorldStats
import com.parassidhu.coronavirusapp.util.NetworkResult
import com.parassidhu.coronavirusapp.util.safeApiCall
import javax.inject.Inject

class MainRepo @Inject constructor(
    private val apiClient: ApiClient,
    private val dao: CountryDao
){

    suspend fun getCountryWiseCases(): NetworkResult<CountryWiseCase> {
        var networkResult: NetworkResult<CountryWiseCase>? = null

        safeApiCall( { apiClient.getCountryWiseCases() },
            { networkResult = it },
            { networkResult = it }
        )

        return networkResult!!
    }

    suspend fun getWorldStats(): NetworkResult<WorldStats> {
        var networkResult: NetworkResult<WorldStats>? = null

        safeApiCall( { apiClient.getWorldStats() },
            { networkResult = it },
            { networkResult = it }
        )

        return networkResult!!
    }

    suspend fun insertCountries(list: List<CountryStat>) {
        dao.insert(list)
    }

    fun getCountries(): LiveData<List<CountryStat>> {
        return dao.getCountries()
    }

    suspend fun addToFavorite(countryStat: FavoriteCountry) {
        dao.addToFavorite(listOf(countryStat))
    }

    suspend fun removeFromFavorite(countryStat: FavoriteCountry) {
        dao.removeFromFavorite(countryStat)
    }

    fun getFavorites() = dao.getAllFavorites()
}