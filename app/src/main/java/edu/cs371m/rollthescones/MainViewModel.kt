package edu.cs371m.rollthescones

import android.location.Location
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {
    private val placesApi = PlacesApi.create()
    private val placeRepository = PlacesRepository(placesApi)
    val places = MutableLiveData<List<Restaurant>>()
    var easierRestUsed = MutableLiveData<Boolean>().apply {
        value = false
    }
    var latLong = MutableLiveData<String>().apply {
        value = ""
    }
    var radius = MutableLiveData<Int>().apply {
        value = 5000
    }
    var restaurantType = MutableLiveData<String>().apply {
        value = "american"
    }
    var restaurantTypeEasier = MutableLiveData<String>().apply {
        value = "american"
    }
    var minPrice = MutableLiveData<Int>().apply {
        value = 0
    }
    var maxPrice = MutableLiveData<Int>().apply {
        value = 4
    }

    var minRating = MutableLiveData<Int>().apply {
        value = 0
    }
    fun netFetchRestaurant() = viewModelScope.launch (
        context = viewModelScope.coroutineContext + Dispatchers.IO) {
        places.postValue(placeRepository.getRestaurants(latLong.value!!, radius.value!!, minPrice.value!!, maxPrice.value!!, restaurantType.value!!))
    }

    fun netFetchRestaurantEasier() = viewModelScope.launch (
        context = viewModelScope.coroutineContext + Dispatchers.IO) {
        places.postValue(placeRepository.getRestaurantsEasier(latLong.value!!, radius.value!!, minPrice.value!!, maxPrice.value!!, restaurantType.value!!))
    }

    fun observePlaces():LiveData<List<Restaurant>> {
        return places
    }

    fun observeRadius():LiveData<Int> {
        return radius
    }

    fun observeMinPrice():LiveData<Int> {
        return minPrice
    }
    fun observeMaxPrice():LiveData<Int> {
        return maxPrice
    }
}