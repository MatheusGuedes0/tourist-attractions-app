package utfpr.edu.br.tourist_attractions_app

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import utfpr.edu.br.tourist_attractions_app.data.DatabaseHandler
import utfpr.edu.br.tourist_attractions_app.databinding.ActivityCadastroBinding
import utfpr.edu.br.tourist_attractions_app.model.PontoTuristico

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    selectedImageUri = result.data?.data
                    binding.imageViewFoto.setImageURI(selectedImageUri)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(result.data), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    private val requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) openImagePicker()
            else Toast.makeText(this, "Permissão negada! 🙁", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelecionarImagem.setOnClickListener {
            checkGalleryPermissionAndPick()
        }

        binding.btnSalvar.setOnClickListener {
            salvarCadastro()
        }
    }

    private fun checkGalleryPermissionAndPick() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> openImagePicker()

            shouldShowRequestPermissionRationale(permission) ->
                requestGalleryPermission.launch(permission)

            else -> requestGalleryPermission.launch(permission)
        }
    }

    private fun openImagePicker() {
        ImagePicker.with(this)
            .cropSquare()
            .compress(1024)
            .galleryOnly()
            .createIntent { intent -> pickImage.launch(intent) }
    }

    private fun salvarCadastro() {

        val nome      = binding.editTextNome.text.toString().trim()
        val descricao = binding.editTextDescricao.text.toString().trim()
        val latStr    = binding.editTextLatitude.text.toString().trim()
        val lonStr    = binding.editTextLongitude.text.toString().trim()

        /* ---------- validações bem simples (apenas Toast) ---------- */
        if (nome.length < 3 || nome.all { it.isDigit() }) {
            Toast.makeText(this, "Nome precisa ter ao menos 3 letras.", Toast.LENGTH_SHORT).show()
            return
        }

        if (descricao.isBlank()) {
            Toast.makeText(this, "Descrição é obrigatória.", Toast.LENGTH_SHORT).show()
            return
        }

        val latitude  = latStr.toDoubleOrNull()
        if (latitude == null || latitude !in -90.0..90.0) {
            Toast.makeText(this, "Latitude deve estar entre -90 e 90.", Toast.LENGTH_SHORT).show()
            return
        }

        val longitude = lonStr.toDoubleOrNull()
        if (longitude == null || longitude !in -180.0..180.0) {
            Toast.makeText(this, "Longitude deve estar entre -180 e 180.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Selecione uma imagem.", Toast.LENGTH_SHORT).show()
            return
        }

        // ---------- persiste ----------
        DatabaseHandler(this).insert(
            PontoTuristico(
                nome       = nome,
                descricao  = descricao,
                latitude   = latitude,
                longitude  = longitude,
                imagemUri  = selectedImageUri.toString()
            )
        )

        Toast.makeText(this, "Ponto Turístico salvo com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

}
