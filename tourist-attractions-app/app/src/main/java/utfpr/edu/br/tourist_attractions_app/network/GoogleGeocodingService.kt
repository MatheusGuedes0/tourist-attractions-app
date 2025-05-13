package utfpr.edu.br.tourist_attractions_app.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleGeocodingService {
    @GET("json")
    fun getCoordinates(
        @Query("address") address: String,
        @Query("AIzaSyD44-7-TlqCJ1Q-KiaXbCrYIwkAkmy3jf0") apiKey: String
    ): Call<GeocodingResponse>

    @GET("json")
    fun getAddress(
        @Query("latlng") latlng: String, // Ex: "-23.5505,-46.6333"
        @Query("AIzaSyD44-7-TlqCJ1Q-KiaXbCrYIwkAkmy3jf0") apiKey: String
    ): Call<GeocodingResponse>
}

