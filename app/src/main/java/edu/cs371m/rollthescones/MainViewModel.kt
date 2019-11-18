package edu.cs371m.rollthescones

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

class MainViewModel : ViewModel() {
    private val placesApi = PlacesApi.create()
    private val placeRepository = PlacesRepository(placesApi)
    private val places = MutableLiveData<List<Restaurant>>()
    var latLong = MutableLiveData<String>().apply {
        value = ""
    }
    fun netFetchRestaurant() = viewModelScope.launch (
        context = viewModelScope.coroutineContext + Dispatchers.IO) {
        places.postValue(placeRepository.getRestaurants(latLong.value!!, 5000))
    }

    fun observePlaces():LiveData<List<Restaurant>> {
        return places
    }


}