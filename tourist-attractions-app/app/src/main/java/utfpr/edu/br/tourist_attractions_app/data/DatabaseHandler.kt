package utfpr.edu.br.tourist_attractions_app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import utfpr.edu.br.tourist_attractions_app.model.PontoTuristico

class DatabaseHandler (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                descricao TEXT,
                latitude REAL,
                longitude REAL,
                imagemUri TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(ponto: PontoTuristico) {
        val values = ContentValues().apply {
            put("nome", ponto.nome)
            put("descricao", ponto.descricao)
            put("latitude", ponto.latitude)
            put("longitude", ponto.longitude)
            put("imagemUri", ponto.imagemUri)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun update(ponto: PontoTuristico) {
        val values = ContentValues().apply {
            put("nome", ponto.nome)
            put("descricao", ponto.descricao)
            put("latitude", ponto.latitude)
            put("longitude", ponto.longitude)
            put("imagemUri", ponto.imagemUri)
        }
        writableDatabase.update(TABLE_NAME, values, "id = ?", arrayOf(ponto.id.toString()))
    }

    fun delete(id: Int) {
        writableDatabase.delete(TABLE_NAME, "id = ?", arrayOf(id.toString()))
    }

    fun find(id: Int): PontoTuristico? {
        val cursor = readableDatabase.query(
            TABLE_NAME,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToNext()) {
            PontoTuristico(
                id = cursor.getInt(0),
                nome = cursor.getString(1),
                descricao = cursor.getString(2),
                latitude = cursor.getDouble(3),
                longitude = cursor.getDouble(4),
                imagemUri = cursor.getString(5)
            )
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun list(): List<PontoTuristico> {
        val list = mutableListOf<PontoTuristico>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

        while (cursor.moveToNext()) {
            list.add(
                PontoTuristico(
                    id = cursor.getInt(0),
                    nome = cursor.getString(1),
                    descricao = cursor.getString(2),
                    latitude = cursor.getDouble(3),
                    longitude = cursor.getDouble(4),
                    imagemUri = cursor.getString(5)
                )
            )
        }

        cursor.close()
        return list
    }

    companion object {
        private const val DATABASE_NAME = "pontos_turisticos.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "pontos"
    }
}