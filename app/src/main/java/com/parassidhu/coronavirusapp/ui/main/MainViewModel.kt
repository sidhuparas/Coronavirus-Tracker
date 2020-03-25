package com.parassidhu.coronavirusapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.WorldStats
import com.parassidhu.coronavirusapp.util.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: MainRepo
): ViewModel() {

    init {
        viewModelScope.launch {
            getCountryWiseCases()
            getWorldStats()
        }
    }

    fun getCountryWiseCases() {
        viewModelScope.launch {
            val response = repo.getCountryWiseCases()

            when(response) {
                is NetworkResult.Success -> {
                    val data = response.data
                    _countryWiseCasesResponse.value = data.countryStats
                }

                is NetworkResult.Error -> {
                    _countryWiseCasesResponse.value = null
                }
            }
        }
    }

    fun getWorldStats() {
        viewModelScope.launch {
            val response = repo.getWorldStats()

            when(response) {
                is NetworkResult.Success -> {
                    val data = response.data
                    _worldStats.value = data
                }

                is NetworkResult.Error -> {
                    _worldStats.value = null
                }
            }
        }
    }

    private val _countryWiseCasesResponse = MutableLiveData<List<CountryStat>?>()
    val countryWiseCasesResponse: LiveData<List<CountryStat>?>
        get() = _countryWiseCasesResponse

    private val _worldStats = MutableLiveData<WorldStats?>()
    val worldStats: LiveData<WorldStats?>
        get() = _worldStats
}