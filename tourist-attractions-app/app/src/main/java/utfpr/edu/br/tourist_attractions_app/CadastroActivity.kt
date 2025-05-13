package utfpr.edu.br.tourist_attractions_app

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import utfpr.edu.br.tourist_attractions_app.utils.GeocodingHelper
import utfpr.edu.br.tourist_attractions_app.utils.hasLocationPermission

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private var selectedImageUri: Uri? = null
    private var pontoExistente: PontoTuristico? = null

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

        //Teste realizado por Jonathan para a busca de coordenadas

        binding.btnBuscarCoordenadas.setOnClickListener {
            if (hasLocationPermission()) {
                buscarLocalizacaoAtual()
            } else {
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }


        // 1. Verifica se foi passado um ponto para edição
        pontoExistente = intent.getSerializableExtra("ponto") as? PontoTuristico
        pontoExistente?.let {
            preencherCampos(it)
            binding.btnSalvar.text = "Atualizar"
        }

        // 2. Configura botão de imagem
        binding.btnSelecionarImagem.setOnClickListener {
            checkGalleryPermissionAndPick()
        }

        // 3. Salva ou atualiza, dependendo do modo
        binding.btnSalvar.setOnClickListener {
            if (pontoExistente != null) atualizarCadastro()
            else salvarCadastro()
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

    private fun preencherCampos(ponto: PontoTuristico) {
        binding.editTextNome.setText(ponto.nome)
        binding.editTextDescricao.setText(ponto.descricao)
        binding.editTextLatitude.setText(ponto.latitude.toString())
        binding.editTextLongitude.setText(ponto.longitude.toString())

        selectedImageUri = Uri.parse(ponto.imagemUri)
        binding.imageViewFoto.setImageURI(selectedImageUri)

        binding.btnSalvar.text = "Atualizar"
    }
    private fun atualizarCadastro() {
        val nome = binding.editTextNome.text.toString().trim()
        val descricao = binding.editTextDescricao.text.toString().trim()
        val latitude = binding.editTextLatitude.text.toString().trim().toDoubleOrNull()
        val longitude = binding.editTextLongitude.text.toString().trim().toDoubleOrNull()

        if (nome.length < 3 || descricao.isBlank() || latitude == null || longitude == null || selectedImageUri == null) {
            Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val pontoAtualizado = PontoTuristico(
            id = pontoExistente!!.id,
            nome = nome,
            descricao = descricao,
            latitude = latitude,
            longitude = longitude,
            imagemUri = selectedImageUri.toString()
        )

        DatabaseHandler(this).update(pontoAtualizado)
        Toast.makeText(this, "Ponto atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun btEditarOnClick(view: View, ponto: PontoTuristico) {
        val intent = Intent(view.context, CadastroActivity::class.java)
        intent.putExtra("ponto", ponto) // o ponto precisa ser Serializable
        view.context.startActivity(intent)
    }


    //Mechido por cassol

    // Adicione no início da classe:
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                buscarLocalizacaoAtual()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                buscarLocalizacaoAtual()
            }
            else -> {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Novo metodo para buscar localização
    private fun buscarLocalizacaoAtual() {
        binding.btnBuscarCoordenadas.isEnabled = false
        binding.btnBuscarCoordenadas.text = "Obtendo localização..."

        GeocodingHelper().getCurrentLocation(
            context = this,
            onSuccess = { lat, lng ->
                runOnUiThread {
                    binding.editTextLatitude.setText(lat.toString())
                    binding.editTextLongitude.setText(lng.toString())
                    binding.btnBuscarCoordenadas.isEnabled = true
                    binding.btnBuscarCoordenadas.text = "Buscar Novamente"
                    Toast.makeText(this, "Localização obtida!", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { error ->
                runOnUiThread {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    binding.btnBuscarCoordenadas.isEnabled = true
                    binding.btnBuscarCoordenadas.text = "Tentar Novamente"
                }
            }
        )
    }



// Modifique o clique do botão:

}
