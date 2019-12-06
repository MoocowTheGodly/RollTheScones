package edu.cs371m.rollthescones

class PlacesRepository (private val placesApi: PlacesApi) {

    suspend fun getRestaurants(location: String, radius: Int, minPrice: Int, maxPrice: Int, query: String): List<Restaurant>? {
        val response = placesApi.getNearbyPlaces(location, radius, "restaurant", "AIzaSyCGasbYUduKIicNs7RjPWXxaefdiqHhEew", query+" sit down restaurants", minPrice, maxPrice, true)
        var restaurants = mutableListOf<Restaurant>()
        val size = response.results.size
        for (i in 0 until size) {
            restaurants.add(response.results[i])
        }
        return restaurants
    }
    suspend fun getRestaurantsEasier(location: String, radius: Int, minPrice: Int, maxPrice: Int, query: String): List<Restaurant>? {
        val response = placesApi.getNearbyPlaces(location, radius, "restaurant", "AIzaSyCGasbYUduKIicNs7RjPWXxaefdiqHhEew", query+" food", minPrice, maxPrice, true)
        var restaurants = mutableListOf<Restaurant>()
        val size = response.results.size
        for (i in 0 until size) {
            restaurants.add(response.results[i])
        }
        return restaurants
    }
}