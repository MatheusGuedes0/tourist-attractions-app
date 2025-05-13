package utfpr.edu.br.tourist_attractions_app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"

    val geocodingService: GoogleGeocodingService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleGeocodingService::class.java)
    }
}
