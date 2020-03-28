package com.parassidhu.coronavirusapp.util

import com.parassidhu.coronavirusapp.BuildConfig

object Constants {

    const val API_KEY = BuildConfig.API_KEY

    const val MASK_INSTRUCTIONS_URL = BuildConfig.BASE_URL + "coronavirus/masks.php"
    const val INDIAN_DATA_URL = "https://api.covid19india.org/data.json"

    const val BANNER_JSON = "banner_json"
}