package edu.cs371m.rollthescones

import com.google.android.gms.common.api.Result
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApi {


    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int?,
        @Query("types") types: String,
        @Query("key") key: String): Place


    /*
    //temporary hardcode for now
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyCGasbYUduKIicNs7RjPWXxaefdiqHhEew")
    fun getNearbyPlaces (): Call<PlaceResponse>
    */


    companion object {
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
            return GsonConverterFactory.create(gsonBuilder.create())
        }
        //private const val BASE_URL = "https://www.maps.googleapis.com/"
        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host("maps.googleapis.com")
            .build()
        fun create(): PlacesApi = create(httpurl)
        private fun create(httpUrl: HttpUrl): PlacesApi {

            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory())
                .build()
                .create(PlacesApi::class.java)
        }
    }
}