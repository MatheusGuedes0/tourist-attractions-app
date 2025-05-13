package utfpr.edu.br.tourist_attractions_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import utfpr.edu.br.tourist_attractions_app.data.DatabaseHandler

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapa: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapaFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap

        // Lê as preferências da usuária
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val tipoMapa = prefs.getString("tipo_mapa", "normal")
        val zoom = prefs.getString("zoom_mapa", "15")?.toFloatOrNull() ?: 15f

        //Define o tipo do mapa
        mapa.mapType = when (tipoMapa) {
            "satellite" -> GoogleMap.MAP_TYPE_SATELLITE
            "hybrid" -> GoogleMap.MAP_TYPE_HYBRID
            "terrain" -> GoogleMap.MAP_TYPE_TERRAIN
            else -> GoogleMap.MAP_TYPE_NORMAL
        }

        // Centraliza o mapa
        val centro = LatLng(-14.2350, -51.9253)
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(centro, zoom))

        // Recupera e exibe os pontos turísticos salvos
        val db = DatabaseHandler(this)
        val pontos = db.list()

        for (ponto in pontos) {
            val local = LatLng(ponto.latitude, ponto.longitude)
            mapa.addMarker(
                MarkerOptions()
                    .position(local)
                    .title(ponto.nome)
                    .snippet(ponto.descricao)
            )
        }
    }

}
