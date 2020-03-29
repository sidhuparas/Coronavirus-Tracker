package com.parassidhu.coronavirusapp.ui.main

import androidx.lifecycle.LiveData
import com.parassidhu.coronavirusapp.db.CountryDao
import com.parassidhu.coronavirusapp.network.ApiClient
import com.parassidhu.coronavirusapp.network.response.*
import com.parassidhu.coronavirusapp.util.NetworkResult
import com.parassidhu.coronavirusapp.util.Utils
import com.parassidhu.coronavirusapp.util.log
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

    suspend fun getStatewiseStats(): NetworkResult<StatewiseResponse> {
        var networkResult: NetworkResult<StatewiseResponse>? = null

        safeApiCall( { apiClient.getStatewiseStats() },
            { networkResult = it },
            { networkResult = it }
        )

        return networkResult!!
    }

    suspend fun insertCountries(list: List<CountryStat>) {
        dao.insert(list)
        list.forEach {
            if (dao.checkFavorite(it.countryName) > 0) {
                log(it.countryName)
                dao.addToFavorite(Utils.toFavorite(it))
            }
        }
    }

    fun getCountries(): LiveData<List<CountryStat>> {
        return dao.getCountries()
    }

    suspend fun addToFavorite(countryStat: FavoriteCountry) {
        dao.addToFavorite(countryStat)
    }

    suspend fun removeFromFavorite(countryStat: FavoriteCountry) {
        dao.removeFromFavorite(countryStat)
    }

    fun getFavorites() = dao.getAllFavorites()
}