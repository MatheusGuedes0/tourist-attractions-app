package utfpr.edu.br.tourist_attractions_app.utils


import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import utfpr.edu.br.tourist_attractions_app.CadastroActivity
import utfpr.edu.br.tourist_attractions_app.R
import utfpr.edu.br.tourist_attractions_app.data.DatabaseHandler
import utfpr.edu.br.tourist_attractions_app.model.PontoTuristico
import java.util.Locale

class PontoTuristicoAdapter(
    private var lista: MutableList<PontoTuristico>,
    private val context: Context
) : RecyclerView.Adapter<PontoTuristicoAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titulo: TextView = itemView.findViewById(R.id.tituloTextView)
        val coords: TextView = itemView.findViewById(R.id.coordsTextView)
        val descricao: TextView = itemView.findViewById(R.id.descricaoTextView)
        val btnExcluir: ImageButton = itemView.findViewById(R.id.btnExcluir)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ponto = lista[position]

        holder.titulo.text = ponto.nome
        val endereco = getEnderecoSimples(holder.itemView.context, ponto.latitude, ponto.longitude)
        holder.coords.text = endereco

        holder.descricao.text = ponto.descricao

        if (!ponto.imagemUri.isNullOrEmpty()) {
            holder.imageView.setImageURI(Uri.parse(ponto.imagemUri))
        }

        holder.btnExcluir.setOnClickListener {

            val db = DatabaseHandler(context)
            db.delete(ponto.id)

            lista.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, lista.size)
        }

        holder.btnEditar.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CadastroActivity::class.java)
            intent.putExtra("ponto", ponto) // ponto é o item da lista
            context.startActivity(intent)
        }

    }

    fun atualizarLista(novaLista: List<PontoTuristico>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }


    fun getEnderecoSimples(context: Context, latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val lista = geocoder.getFromLocation(latitude, longitude, 1)
            if (!lista.isNullOrEmpty()) {
                val endereco = lista[0]
                val logradouro = endereco.thoroughfare ?: ""
                val cidade = endereco.locality ?: ""
                val estado = endereco.adminArea ?: ""
                "$logradouro, $cidade - $estado"
            } else {
                "Endereço não encontrado"
            }
        } catch (e: Exception) {
            Log.e("Geocoder", "Erro ao obter endereço: ${e.message}")
            "Erro ao obter endereço"
        }
    }


}
