package edu.cs371m.rollthescones

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName


//TODO: Maybe add more fields/variables?? https://stackoverflow.com/questions/32393134/getting-results-of-nearby-places-from-users-location-using-google-maps-api-in-a
data class Restaurant (
    @SerializedName("geometry")
    val geometry: LatLng,
    @SerializedName("name")
    val name: String,
    @SerializedName("formatted_address")
    val address: String,
    @SerializedName("rating")
    val rating: Double

)
