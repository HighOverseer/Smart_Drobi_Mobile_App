package com.smartdrobi.aplikasipkm.ui.addbridge

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ActivityAddBridgeFormBinding
import com.smartdrobi.aplikasipkm.domain.helper.AUTHORITY
import com.smartdrobi.aplikasipkm.domain.helper.JAKARTA_LAT
import com.smartdrobi.aplikasipkm.domain.helper.JAKARTA_LON
import com.smartdrobi.aplikasipkm.domain.helper.createCustomTempFile
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.domain.helper.obtainViewModel
import com.smartdrobi.aplikasipkm.domain.helper.setInit
import com.smartdrobi.aplikasipkm.domain.helper.showToast
import com.smartdrobi.aplikasipkm.domain.helper.uriToFile
import com.smartdrobi.aplikasipkm.domain.model.AddBridgeSaveState
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.domain.model.BridgeMaterial
import com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel.AddBridgeFormViewModel
import com.smartdrobi.aplikasipkm.ui.home.HomeActivity
import com.smartdrobi.aplikasipkm.ui.map.MapsActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

class AddBridgeFormActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddBridgeFormBinding
    private lateinit var viewModel: AddBridgeFormViewModel
    private lateinit var mMap: GoogleMap

    //container variabel for saving path on intent Camera
    private var currSelectedPhotoPath: String? = null


    private var saveState = AddBridgeSaveState()
    private var isStateRetrieved = false

    //for intent gallery
    private var uriToFileJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBridgeFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = obtainViewModel<
                AddBridgeFormViewModel.ViewModelFactory,
                AddBridgeFormViewModel
                >(this, applicationContext)
        retrieveState(savedInstanceState)
        initViewComponents()
        observeEvent()
    }


    private fun initViewComponents() {
        initMap()
        initForm()
        initButtons()
        initEditTexts()
        initSpinner()
    }

    private fun observeEvent() {
        viewModel.apply {
            bridgeAddedEvent.observe(this@AddBridgeFormActivity) {
                it.getContentIfNotHandled()?.let {
                    showToast(
                        this@AddBridgeFormActivity,
                        getString(R.string.data_jembatan_berhasil_disimpan)
                    )
                    setResult(HomeActivity.ADD_BRIDGE_RESULT_SUCCESS)
                    finish()
                }
            }

            isLoading.observe(this@AddBridgeFormActivity) { isVisible ->
                binding.progressBar.isVisible = isVisible
            }
        }
    }


    private fun retrieveState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            saveState = it.getDataParcel(SAVE_STATE_KEY, AddBridgeSaveState::class.java) ?: return

            val state = saveState

            binding.apply {
                state.imagePath?.let { path ->
                    ivImage.loadImage(
                        this@AddBridgeFormActivity,
                        path
                    )
                }
                //flag to restore map state when map is ready
                isStateRetrieved = true
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun <T> Bundle.getDataParcel(
        key: String,
        clazz: Class<T>
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, clazz)
        } else {
            getParcelable(key)
        }
    }

    private fun initEditTexts() {
        binding.apply {
            etBuildDate.setOnClickListener {
                clearAllViewFocus()
                showDatePicker(it)
            }
            etCheckDate.setOnClickListener {
                clearAllViewFocus()
                showDatePicker(it)
            }
        }
    }

    private fun initForm() {
        binding.clForm.setOnClickListener {
            clearAllViewFocus()
        }
    }

    private fun clearAllViewFocus() {
        binding.root.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun initSpinner() {
        binding.apply {
            val materials = resources.getStringArray(R.array.bridge_material_list).toList()
            spMaterial.setInit(this@AddBridgeFormActivity, materials)
        }
    }

    private fun initButtons() {
        binding.apply {
            onBackPressedDispatcher.addCallback(
                this@AddBridgeFormActivity,
                onBackPressedCallback
            )
            btnCancel.setOnClickListener {
                finish()
            }
            btnChooseLocation.setOnClickListener {
                clearAllViewFocus()
                toMapsActivity()
            }
            btnChooseImage.setOnClickListener {
                clearAllViewFocus()
                showDialogIntentPhoto()
            }
            btnSave.setOnClickListener {
                clearAllViewFocus()
                saveBridge()
            }
        }
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            saveState.imagePath?.let {
                File(it).apply {
                    if (exists()) delete()
                }
            }
            finish()
        }
    }

    private fun saveBridge() {
        var messageError: String? = null

        binding.apply out@{
            val imagePath = saveState.imagePath ?: return@out
            val name = etName.text ?: return@out
            val nationalNum = etNoNational.text ?: return@out
            val cityNum = etNoCity.text ?: return@out
            val tollNum = etNoToll.text ?: return@out
            val buildDate = etBuildDate.text ?: return@out
            val lat = saveState.position.latitude
            val lon = saveState.position.longitude
            val mapLocName = saveState.mapLocName
            val provinceCity = etProvinceCity.text ?: return@out
            val length = etLength.text ?: return@out
            val wide = etWidth.text ?: return@out
            val inspectPlanDate = etCheckDate.text ?: return@out
            val material = when (spMaterial.selectedItemPosition) {
                1 -> BridgeMaterial.Wood
                2 -> BridgeMaterial.Iron
                3 -> BridgeMaterial.Steel
                else -> return@out
            }
            try {

                viewModel.insertBridge(
                    Bridge(
                        imagePath = imagePath,
                        name = name.toString(),
                        nationalNumber = nationalNum.toString().toInt(),
                        cityNumber = cityNum.toString().toInt(),
                        tollNumber = tollNum.toString().toInt(),
                        buildDate = buildDate.toString(),
                        latitudePosition = lat.toString().toDouble(),
                        longitudePosition = lon.toString().toDouble(),
                        mapLocName = mapLocName,
                        provinceCity = provinceCity.toString(),
                        length = length.toString().toDouble(),
                        wide = wide.toString().toDouble(),
                        inspectionPlanDate = inspectPlanDate.toString(),
                        bridgeMaterial = material
                    )
                )
                return

            } catch (e: Exception) {
                messageError = e.message.toString()
                return@out
            }
        }
        messageError?.let {
            showToast(
                this,
                it
            )
        } ?: showToast(
            this,
            getString(R.string.tolong_lengkapi_formnya)
        )

    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.container_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun showDatePicker(clickedView: View) {
        val editText = clickedView as AppCompatEditText
        val currSelectedDate = editText.text.toString()

        val year: Int
        val month: Int
        val day: Int

        if (currSelectedDate.isBlank()) {
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        } else {
            val splittedDate = currSelectedDate.split("/")
            day = splittedDate[0].toInt()
            month = splittedDate[1].toInt() - 1
            year = splittedDate[2].toInt()
        }

        DatePickerDialog(this, { _, yearr, monthOfTheYear, dayOfTheMonth ->
            editText.setText(
                getString(
                    R.string.date,
                    dayOfTheMonth.toString(),
                    monthOfTheYear.plus(1).toString(),
                    yearr.toString()
                )
            )
        }, year, month, day).show()
    }


    private fun showDialogIntentPhoto() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.pilih_metode))
            .setItems(resources.getStringArray(R.array.intent_photo_options)) { dialog, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
                dialog.dismiss()
            }.create()
            .show()
    }

    private fun toMapsActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        mapActivityLauncher.launch(intent)
    }

    override fun onMapReady(gmap: GoogleMap) {
        mMap = gmap
        mMap.apply {
            uiSettings.isMapToolbarEnabled = false
            uiSettings.setAllGesturesEnabled(false)

            if (!isStateRetrieved) {
                val jakarta = LatLng(JAKARTA_LAT, JAKARTA_LON)
                addMarker(
                    MarkerOptions()
                        .position(jakarta)
                )
                animateCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 5f))
                return
            }

            val state = saveState
            clear()
            addMarker(
                MarkerOptions()
                    .position(state.position)
            )
            animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    state.position,
                    20f
                )
            )
        }


    }

    private val mapActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ACTIVITY_MAP_RESULT_CODE) {
            result.data?.also {
                mMap.clear()
                val selectedLat = it.getDoubleExtra(LAT_KEY, JAKARTA_LAT)
                val selectedLon = it.getDoubleExtra(LON_KEY, JAKARTA_LON)
                val mapLocName = it.getStringExtra(LOC_NAME_KEY) ?: return@also
                val position = LatLng(selectedLat, selectedLon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        position,
                        20f
                    )
                )
                saveState = saveState.copy(
                    position = position,
                    mapLocName = mapLocName
                )
            }

        }
    }

    private fun openGallery() {
        Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            val chooser = Intent.createChooser(
                this,
                getString(R.string.choose_a_picture)
            )
            intentChooseImageLauncher.launch(chooser)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoUri = FileProvider.getUriForFile(
                this,
                AUTHORITY,
                it
            )
            currSelectedPhotoPath = it.absolutePath
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                photoUri
            )
            intentChooseImageLauncher.launch(intent)
        }
    }

    private val intentChooseImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {

                uriToFileJob?.cancel()
                uriToFileJob = lifecycleScope.launch {
                    val file = uriToFile(it, this@AddBridgeFormActivity) ?: return@launch

                    binding.ivImage.loadImage(
                        this@AddBridgeFormActivity,
                        file
                    )

                    saveState.imagePath?.let { path ->
                        File(path).apply {
                            if (exists()) delete()
                        }
                    }
                    saveState = saveState.copy(
                        imagePath = file.absolutePath
                    )
                }
            } ?: currSelectedPhotoPath?.let {
                binding.ivImage.loadImage(
                    this,
                    it
                )

                saveState.imagePath?.let { path ->
                    File(path).apply {
                        if (exists()) delete()
                    }
                }
                saveState = saveState.copy(
                    imagePath = it
                )
            } ?: return@registerForActivityResult

        } else if (result.resultCode == RESULT_CANCELED) {
            result.data?.data?.let { imageUri ->
                val file = imageUri.path?.let { File(it) }
                file?.delete()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        uriToFileJob?.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVE_STATE_KEY, saveState)
    }


    companion object {
        const val ACTIVITY_MAP_RESULT_CODE = 100
        const val LAT_KEY = "lat_key"
        const val LON_KEY = "lon_key"
        const val LOC_NAME_KEY = "loc_key"

        private const val SAVE_STATE_KEY = "save"

    }


}