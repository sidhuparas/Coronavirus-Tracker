package com.parassidhu.coronavirusapp.network

import com.parassidhu.coronavirusapp.network.response.CountryWiseCase
import com.parassidhu.coronavirusapp.network.response.StatewiseResponse
import com.parassidhu.coronavirusapp.network.response.WorldStats
import com.parassidhu.coronavirusapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Url

@JvmSuppressWildcards
interface ApiClient {

    @GET("coronavirus/cases_by_country.php")
    suspend fun getCountryWiseCases(): CountryWiseCase

    @GET("coronavirus/worldstat.php")
    suspend fun getWorldStats(): WorldStats

    @GET
    suspend fun getStatewiseStats(@Url url: String = Constants.INDIAN_DATA_URL): StatewiseResponse
}