package com.parassidhu.coronavirusapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.parassidhu.coronavirusapp.network.response.*
import com.parassidhu.coronavirusapp.util.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

class MainViewModel @Inject constructor(
    private val repo: MainRepo,
    private val remoteConfig: FirebaseRemoteConfig,
    private val gson: Gson
): ViewModel() {

    init {
        viewModelScope.launch {
            getCountryWiseCases()
            getStatewiseStats()
            getBanners()
            getWorldStats()
        }
    }

    val combinedLiveData = CombinedLiveData(getFavorites(),
        getCountryLiveData()) { list1: List<FavoriteCountry>?, list2: List<CountryStat>? ->
        val finalList = mutableListOf<BaseCountryResponse>()
        if (list1 != null && list2 != null) {
            finalList.addAll(list1)
            finalList.addAll(list2)

            return@CombinedLiveData finalList.distinctBy {
                if (it is FavoriteCountry)
                    it.countryName
                else
                    (it as CountryStat).countryName
            }
        } else {
            return@CombinedLiveData listOf<CountryStat>()
        }
    }

    private val _worldStats = MutableLiveData<WorldStats>()
    val worldStats: LiveData<WorldStats>
        get() = _worldStats

    private val _stateResponse = MutableLiveData<List<StatewiseResult>>()
    val stateResponse: LiveData<List<StatewiseResult>>
        get() = _stateResponse

    private val _bannerResponse = MutableLiveData<List<BannerResult>>()
    val bannerResponse: LiveData<List<BannerResult>>
        get() = _bannerResponse

    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: LiveData<String?>
        get() = _errorLiveData

    fun getCountryWiseCases() {
        viewModelScope.launch {
            val response = repo.getCountryWiseCases()

            when(response) {
                is NetworkResult.Success -> {
                    val data = response.data
                    insertCountries(data.countryStats)
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

    private fun getStatewiseStats() {
        viewModelScope.launch {
            val response = repo.getStatewiseStats()

            when(response) {
                is NetworkResult.Success -> {
                    val data = response.data.data
                    _stateResponse.value = data
                }

                is NetworkResult.Error -> {
                    val data = response.exception
                    _errorLiveData.value = data
                }
            }
        }
    }

    private fun getBanners() {
        viewModelScope.launch {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isComplete) {
                    val data = remoteConfig.getString(Constants.BANNER_JSON)
                    try {
                        _bannerResponse.value = gson.fromJson<BannerResponse>(data).data
                    } catch (e: Exception) {
                        Log.e("Banner Error", e.message)
                    }
                }
            }
        }
    }

    private fun insertCountries(list: List<CountryStat>) {
        viewModelScope.launch {
            repo.insertCountries(list)
        }
    }

    fun addToFavorite(countryStat: FavoriteCountry) {
        viewModelScope.launch {
            repo.addToFavorite(countryStat)
        }
    }

    fun removeFromFavorite(countryStat: FavoriteCountry) {
        viewModelScope.launch {
            repo.removeFromFavorite(countryStat)
        }
    }

    private fun getCountryLiveData() = repo.getCountries()

    private fun getFavorites() = repo.getFavorites()
}