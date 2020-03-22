package com.parassidhu.coronavirusapp.network

import com.parassidhu.coronavirusapp.network.response.CountryWiseCase
import com.parassidhu.coronavirusapp.network.response.WorldStats
import retrofit2.http.GET

@JvmSuppressWildcards
interface ApiClient {

    @GET("coronavirus/cases_by_country.php")
    suspend fun getCountryWiseCases(): CountryWiseCase

    @GET("coronavirus/worldstat.php")
    suspend fun getWorldStats(): WorldStats
}