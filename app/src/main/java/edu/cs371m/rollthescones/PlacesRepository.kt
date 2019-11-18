package edu.cs371m.rollthescones

import com.google.gson.Gson

class PlacesRepository (private val placesApi: PlacesApi) {

    val gson = Gson()

    /*
    fun unpackRestaurants (response: Place): List<Restaurant>? {
        var restaurants = mutableListOf<Restaurant>()
        val size = response.results.size
        for (i in 0 until size) {
            restaurants.add(response.results[i])
        }
        return restaurants
    }
     */


    suspend fun getRestaurants(location: String, radius: Int): List<Restaurant>? {
        val response = placesApi.getNearbyPlaces(location, radius, "restaurant", "AIzaSyCGasbYUduKIicNs7RjPWXxaefdiqHhEew")
        var restaurants = mutableListOf<Restaurant>()
        val size = response.results.size
        for (i in 0 until size) {
            restaurants.add(response.results[i])
        }
        return restaurants
    }
}