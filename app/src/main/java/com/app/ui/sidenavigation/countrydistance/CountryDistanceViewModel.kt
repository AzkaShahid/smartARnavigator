package com.app.ui.sidenavigation.countrydistance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.ar.LocationModel
import com.app.bases.BaseViewModel
import com.app.models.countries.CountriesResponseModel
import com.app.models.login.LoginUserResponse
import com.app.network.Resource
import com.app.network.Resource.Success
import com.app.respository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDistanceViewModel @Inject constructor(
    private val repository: MainRepository
) : BaseViewModel() {


    val countryResponse: MutableLiveData<Resource<CountriesResponseModel>> = MutableLiveData()

    fun callGetCountryList() = viewModelScope.launch {
        countryResponse.value = Resource.Loading
        countryResponse.value = repository.callCountries()
    }
}