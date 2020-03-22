package com.parassidhu.coronavirusapp.util

import com.parassidhu.coronavirusapp.network.response.WorldStats

object Utils {

    fun provideBarWeights(response: WorldStats): Triple<Float, Float, Float> {
        try {
            var yellowVal = response.totalCases.replace(",", "").toFloat()
            var greenVal = response.totalRecovered.replace(",", "").toFloat()
            var redVal = response.totalDeath.replace(",", "").toFloat()

            while (yellowVal > 1f && greenVal > 1f && redVal > 1f) {
                yellowVal /= 2f
                greenVal /= 2f
                redVal /= 2f
            }

            return Triple(yellowVal, greenVal, redVal)
        } catch (e: Exception) { // If API response is incorrect
            e.printStackTrace()
            return Triple(1f,1f,1f)
        }
    }
}