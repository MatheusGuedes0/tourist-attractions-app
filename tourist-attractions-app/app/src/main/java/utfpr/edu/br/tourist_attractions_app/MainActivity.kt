package utfpr.edu.br.tourist_attractions_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import utfpr.edu.br.tourist_attractions_app.data.DatabaseHandler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            🔧 BLOCO DE TESTE: OPERACOES NO BANCO DE DADOS

            Esse trecho serve apenas para testar as funcionalidades do banco (inserir, listar, atualizar, buscar e deletar)
            enquanto a interface visual (formulários e lista de pontos turísticos) ainda não foi implementada.

            ⚠️ Pode ser removido ou desativado assim que a interface estiver pronta!
         */

        val db = DatabaseHandler(this)

    // // Inserir novo ponto turístico
    // val ponto = PontoTuristico(
    //     0,
    //     "Cristo Redentor",
    //     "Famoso ponto turístico do RJ",
    //     -22.9519,
    //     -43.2105,
    //     "content://imagem/uri"
    // )
    // db.insert(ponto)

    // // Buscar e exibir todos os pontos cadastrados
    // val lista = db.list()
    // lista.forEach { Log.d("PONTO", it.toString()) }

    // // Atualizar o último ponto inserido
    // val ultimo = lista.last()
    // val pontoAtualizado = ultimo.copy(nome = "Cristo RJ Atualizado")
    // db.update(pontoAtualizado)

    // // Buscar um ponto por ID
    // val buscado = db.find(ultimo.id)
    // Log.d("ENCONTRADO", buscado?.nome ?: "Nada encontrado")

    // // Excluir ponto por ID
    // val idParaExcluir = 6
    // db.delete(idParaExcluir)
    // Log.d("DELETE", "Ponto com ID $idParaExcluir deletado")
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