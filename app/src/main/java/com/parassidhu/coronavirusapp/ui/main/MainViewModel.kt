package com.parassidhu.coronavirusapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.parassidhu.coronavirusapp.network.response.CountryStat
import com.parassidhu.coronavirusapp.network.response.WorldStats
import com.parassidhu.coronavirusapp.util.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: MainRepo,
    private val remoteConfig: FirebaseRemoteConfig
): ViewModel() {

    init {
        viewModelScope.launch {
            getCountryWiseCases()
            getWorldStats()
        }
    }

    private val _countryWiseCasesResponse = MutableLiveData<List<CountryStat>>()
    val countryWiseCasesResponse: LiveData<List<CountryStat>>
        get() = _countryWiseCasesResponse

    private val _worldStats = MutableLiveData<WorldStats>()
    val worldStats: LiveData<WorldStats>
        get() = _worldStats

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?>
        get() = _errorLiveData

    fun getCountryWiseCases() {
        viewModelScope.launch {
            val response = repo.getCountryWiseCases()

            when(response) {
                is NetworkResult.Success -> {
                    val data = response.data
                    _countryWiseCasesResponse.value = data.countryStats
                }

                is NetworkResult.Error -> {
                    val data = response.exception
                    _errorLiveData.value = data
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
                    val data = response.exception
                    _errorLiveData.value = data
                }
            }
        }
    }

    fun getBanners() {
        viewModelScope.launch {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isComplete) {

                }
            }
        }
    }
}