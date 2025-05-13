package utfpr.edu.br.tourist_attractions_app.utils

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.location.Location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utfpr.edu.br.tourist_attractions_app.network.GeocodingResponse
import utfpr.edu.br.tourist_attractions_app.network.ApiClient

class GeocodingHelper {

    /* Metodo para buscar coordenadas por endereço (Geocoding API)
    fun getCoordinates(
        address: String,
        apiKey: String,
        onSuccess: (lat: Double, lng: Double) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        ApiClient.geocodingService.getCoordinates(address, apiKey).enqueue(
            object : Callback<GeocodingResponse> {
                override fun onResponse(
                    call: Call<GeocodingResponse>,
                    response: Response<GeocodingResponse>
                ) {
                    if (response.isSuccessful) {
                        val location = response.body()?.results?.firstOrNull()?.geometry?.location
                        if (location != null) {
                            onSuccess(location.lat, location.lng)
                        } else {
                            onError("Endereço não encontrado")
                        }
                    } else {
                        onError("Erro na API: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                    onError("Falha na conexão: ${t.message}")
                }
            }
        )
    }*/
    fun getAddressFromCoordinates(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        onSuccess: (endereco: String) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        val latLngString = "$latitude,$longitude"

        ApiClient.geocodingService.getAddress(latLngString, apiKey).enqueue(
            object : Callback<GeocodingResponse> {
                override fun onResponse(
                    call: Call<GeocodingResponse>,
                    response: Response<GeocodingResponse>
                ) {
                    if (response.isSuccessful) {
                        val endereco = response.body()?.results?.firstOrNull()?.formatted_address
                        if (endereco != null) {
                            onSuccess(endereco)
                        } else {
                            onError("Endereço não encontrado para estas coordenadas")
                        }
                    } else {
                        onError("Erro na API: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                    onError("Falha na conexão: ${t.message}")
                }
            }
        )
    }

    // Metodo para obter localização atual via GPS
    fun getCurrentLocation(
        context: Context,
        onSuccess: (lat: Double, lng: Double) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        if (!context.hasLocationPermission()) {
            onError("Permissão de localização negada")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            // Tenta obter a última localização conhecida
            val location: Location? = getLastKnownLocation(locationManager)

            if (location != null) {
                onSuccess(location.latitude, location.longitude)
            } else {
                onError("Localização não disponível. Ative o GPS e tente novamente")
            }
        } catch (e: SecurityException) {
            onError("Permissão de localização revogada")
        } catch (e: Exception) {
            onError("Erro no GPS: ${e.message}")
        }
    }

    private fun getLastKnownLocation(locationManager: LocationManager): Location? {
        return try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            null
        }
    }
}

// Extensão para verificar permissões (fora da classe)
fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}