package com.parassidhu.coronavirusapp.network.response

import com.google.gson.annotations.SerializedName

data class StatewiseResponse(
    @SerializedName("statewise") val data: List<StatewiseResult>
)

data class StatewiseResult(
    @SerializedName("confirmed") val totalConfirmed: String,
    @SerializedName("deaths") val totalDeaths: String,
    @SerializedName("recovered") val totalRecovered: String,
    @SerializedName("state") val stateName: String,
    @SerializedName("deltaconfirmed") val deltaConfirmed: Long
): BaseCountryResponse()