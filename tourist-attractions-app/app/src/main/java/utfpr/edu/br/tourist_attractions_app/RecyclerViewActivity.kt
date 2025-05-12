package utfpr.edu.br.tourist_attractions_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import utfpr.edu.br.tourist_attractions_app.data.DatabaseHandler
import utfpr.edu.br.tourist_attractions_app.utils.PontoTuristicoAdapter

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val lista = DatabaseHandler(this).list().toMutableList()
        recyclerView.adapter = PontoTuristicoAdapter(lista, this)
    }

    override fun onResume() {
        super.onResume()
        val lista = DatabaseHandler(this).list()
        (recyclerView.adapter as? PontoTuristicoAdapter)?.atualizarLista(lista)
    }
}
