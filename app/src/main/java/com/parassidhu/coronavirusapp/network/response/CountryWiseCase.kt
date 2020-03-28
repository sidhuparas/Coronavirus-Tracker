package com.parassidhu.coronavirusapp.network.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CountryWiseCase(
    @SerializedName("countries_stat") val countryStats: List<CountryStat>
)

@Entity(tableName = "country_table")
data class CountryStat(

    @PrimaryKey
    @SerializedName("country_name") val countryName: String,

    @SerializedName("cases") val totalCases: String,
    @SerializedName("deaths") val totalDeaths: String,
    @SerializedName("total_recovered") val totalRecovered: String,
    @SerializedName("new_cases") val newCases: String
): BaseCountryResponse()

@Entity(tableName = "fav_country_table")
data class FavoriteCountry(

    @PrimaryKey
    @SerializedName("country_name") val countryName: String,

    @SerializedName("cases") val totalCases: String,
    @SerializedName("deaths") val totalDeaths: String,
    @SerializedName("total_recovered") val totalRecovered: String,
    @SerializedName("new_cases") val newCases: String
): BaseCountryResponse()

open class BaseCountryResponse()