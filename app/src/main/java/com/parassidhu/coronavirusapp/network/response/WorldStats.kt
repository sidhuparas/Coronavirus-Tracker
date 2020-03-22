package com.parassidhu.coronavirusapp.network.response

import com.google.gson.annotations.SerializedName

data class WorldStats(

    @SerializedName("total_cases") val totalCases: String,
    @SerializedName("total_deaths") val totalDeath: String,
    @SerializedName("total_recovered") val totalRecovered: String
)