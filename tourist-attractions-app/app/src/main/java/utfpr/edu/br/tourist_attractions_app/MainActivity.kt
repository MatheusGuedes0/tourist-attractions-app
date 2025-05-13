package utfpr.edu.br.tourist_attractions_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnAbrirMapaOnClick(view: View) {
        val intent = Intent(this, MapaActivity::class.java)
        startActivity(intent)
    }

    fun btTelaConfiguracaoOnClick(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun btTelaCadastroOnClick(view: View) {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
    }

    fun btRecyclerViewOnClick(view: View){
        val intent = Intent(this,RecyclerViewActivity::class.java)
        startActivity((intent))
    }

}