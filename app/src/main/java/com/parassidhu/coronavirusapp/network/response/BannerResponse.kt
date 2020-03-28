package com.parassidhu.coronavirusapp.network.response

import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("data") val data: List<BannerResult>
)

data class BannerResult(
    @SerializedName("image") val image: String,
    @SerializedName("ratio") val ratio: String
)