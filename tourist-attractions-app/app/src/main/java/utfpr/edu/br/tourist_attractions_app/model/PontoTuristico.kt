package utfpr.edu.br.tourist_attractions_app.model

data class PontoTuristico(
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val latitude: Double,
    val longitude: Double,
    val imagemUri: String
)