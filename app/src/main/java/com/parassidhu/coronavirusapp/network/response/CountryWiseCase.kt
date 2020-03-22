package com.parassidhu.coronavirusapp.network.response

import com.google.gson.annotations.SerializedName

data class CountryWiseCase(

    @SerializedName("countries_stat") val countryStats: List<CountryStat>
)

data class CountryStat(

    @SerializedName("country_name") val countryName: String,
    @SerializedName("cases") val totalCases: String,
    @SerializedName("deaths") val totalDeaths: String,
    @SerializedName("total_recovered") val totalRecovered: String,
    @SerializedName("new_cases") val newCases: String
)
