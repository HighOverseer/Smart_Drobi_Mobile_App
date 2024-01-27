package com.smartdrobi.aplikasipkm.ui.addbridge

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.longdo.mjpegviewer.MjpegView
import com.smartdrobi.aplikasipkm.BuildConfig
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ActivityAddBridgeCheckFormBinding
import com.smartdrobi.aplikasipkm.domain.helper.AUTHORITY
import com.smartdrobi.aplikasipkm.domain.helper.createCustomTempFile
import com.smartdrobi.aplikasipkm.domain.helper.obtainViewModel
import com.smartdrobi.aplikasipkm.domain.helper.showDialogConfirmation
import com.smartdrobi.aplikasipkm.domain.helper.uriToFile
import com.smartdrobi.aplikasipkm.domain.model.FieldPositionIntentPhoto
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.IntentPhotoInterface
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BaseFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction
import com.smartdrobi.aplikasipkm.ui.addbridge.uievent.CheckFormUiEvent
import com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel.AddBridgeCheckFormViewModel
import com.smartdrobi.aplikasipkm.ui.dronecam.DroneCamCaptureActivity
import com.smartdrobi.aplikasipkm.ui.dronecam.DroneCamRecordActivity
import com.smartdrobi.aplikasipkm.ui.dronecam.domain.CapturedImageParcelData
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class AddBridgeCheckFormActivity : AppCompatActivity(), IntentPhotoInterface {

    private lateinit var viewModel: AddBridgeCheckFormViewModel

    private lateinit var navController: NavController
    private lateinit var binding: ActivityAddBridgeCheckFormBinding

    //for saving image path intent camera
    private var currSelectedPhotoPath: String? = null

    //just for intent gallery and camera (not for camera drone yet)
    private var fieldPositionIntentPhoto: FieldPositionIntentPhoto? = null

    //for saving state when vm just init for first time, so navigation is in sync
    private var isViewModelStartingSession: Boolean = false

    private var uriToFileJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBridgeCheckFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetrieveState(savedInstanceState)
        viewModel = obtainViewModel<
                AddBridgeCheckFormViewModel.ViewModelFactory,
                AddBridgeCheckFormViewModel
                >(this, applicationContext, intent?.extras)

        /*initRetrieveState(savedInstanceState)
        setUpNavigation()
        initViewDroneCam()*/
        initViewDroneCam()
        observeUiEvents()
        setUpNavigation()



        binding.apply {
            btnCancel.setOnClickListener {
                showDialogConfirmation(
                    this@AddBridgeCheckFormActivity,
                    getString(R.string.yakin_ingin_keluar_dari_halaman_form),
                    { finish() }
                )
            }

            btnBack.setOnClickListener {
                navController.navigateUp()
                //sus
                //navController.popBackStack()
            }

            containerDroneCam.setOnClickListener {
                goToDroneCamRecord()
            }
        }
    }

    private fun observeUiEvents() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is CheckFormUiEvent.StartingSession -> {
                    event {
                        binding.progressBar.isVisible = true
                        isViewModelStartingSession = true
                    }
                }

                is CheckFormUiEvent.NotifyWhenFragmentReadyToInit -> {
                    event {
                        if (isViewModelStartingSession) {
                            binding.progressBar.isVisible = false
                            isViewModelStartingSession = false
                            setUpNavigation()
                        }
                    }
                }

                is CheckFormUiEvent.EndingSession -> {
                    event {
                        setResult(event.resultCode)
                        finish()
                    }
                }

                else -> return@observe
            }
        }
    }


    private fun initRetrieveState(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            fieldPositionIntentPhoto = it.getDataParcel(
                FIELDS_SAVE_KEY,
                FieldPositionIntentPhoto::class.java
            )
            currSelectedPhotoPath = it.getString(IMAGE_PATH_SAVE_KEY, null)
            isViewModelStartingSession = it.getBoolean(VIEW_MODEL_SESSION_SAVE_KEY, false)
        }
    }

    private fun goToDroneCamRecord() {
        val intent = Intent(this, DroneCamRecordActivity::class.java)
        startActivity(intent)
    }

    private fun setUpNavigation() {
        if (!isViewModelStartingSession) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_framgent) as NavHostFragment
            navController = navHostFragment.navController

            navController.removeOnDestinationChangedListener(
                onDestinationChangedListener
            )
            navController.addOnDestinationChangedListener(
                onDestinationChangedListener
            )

            binding.btnContinue.setOnClickListener {
                val currDestinationId =
                    navController.currentDestination?.id ?: return@setOnClickListener
                goToNextPage(currDestinationId)
            }
        }
    }

    private fun goToNextPage(currDestinationId: Int) {
        val nextDestinationId = when (currDestinationId) {
            R.id.addBridgeCheckFragment2 -> R.id.action_addBridgeCheckFragment2_to_bridgeSecurityFormFragment
            R.id.bridgeSecurityFormFragment -> R.id.action_bridgeSecurityFormFragment_to_bridgeSafetyFormFragment
            R.id.bridgeSafetyFormFragment -> R.id.action_bridgeSafetyFormFragment_to_bridgeConvenienceFormFragment
            R.id.bridgeConvenienceFormFragment -> R.id.action_bridgeConvenienceFormFragment_to_bridgeMaintenanceFormFragment
            R.id.bridgeMaintenanceFormFragment -> R.id.action_bridgeMaintenanceFormFragment_to_bridgeSocialFormFragment
            R.id.bridgeSocialFormFragment -> R.id.action_bridgeSocialFormFragment_to_bridgeEmergencyFormFragment
            R.id.bridgeEmergencyFormFragment -> {
                showDialogConfirmation(
                    this,
                    getString(R.string.yakin_ingin_menyimpan_form_ini), {
                        viewModel.sendAction(
                            CheckFormUiAction.FinishSession
                        )
                    }
                )
                return
            }

            else -> return
        }
        navigate(nextDestinationId)
    }


    private fun initViewDroneCam() {
        binding.apply {
            viewDroneCam.apply {
                mode = MjpegView.MODE_STRETCH

                isAdjustHeight = true
                setUrl(BuildConfig.DRONE_CAM_URL)
            }
        }
    }

    private val onDestinationChangedListener by lazy {
        NavController.OnDestinationChangedListener { controller, destination, _ ->
            binding.apply {
                initFormFieldForNextDest(destination.id)
                setBtnNavigationForNextDest(destination.id)
            }
        }
    }

    private fun setBtnNavigationForNextDest(newDestinationId: Int) {
        binding.apply {
            val isFirsPageForm = newDestinationId == R.id.addBridgeCheckFragment2
            val isLastPageForm = newDestinationId == R.id.bridgeEmergencyFormFragment

            if (isFirsPageForm) {
                btnBack.visibility = View.INVISIBLE
                btnBack.isEnabled = false
                btnContinue.text = getText(R.string.selanjutnya)
            } else if (isLastPageForm) {
                btnBack.visibility = View.VISIBLE
                btnBack.isEnabled = true
                btnContinue.text = getText(R.string.simpan)
            } else {
                btnBack.visibility = View.VISIBLE
                btnBack.isEnabled = true
                btnContinue.text = getText(R.string.selanjutnya)
            }
        }
    }

    private fun initFormFieldForNextDest(newDestinationId: Int) {
        val nextDestFormPage = when (newDestinationId) {
            R.id.addBridgeCheckFragment2 -> BaseFormFragment.FormPage.FIRST
            R.id.bridgeConvenienceFormFragment -> BaseFormFragment.FormPage.CONVENIENCE
            R.id.bridgeMaintenanceFormFragment -> BaseFormFragment.FormPage.MAINTENANCE
            R.id.bridgeSafetyFormFragment -> BaseFormFragment.FormPage.SAFETY
            R.id.bridgeSecurityFormFragment -> BaseFormFragment.FormPage.SECURITY
            R.id.bridgeSocialFormFragment -> BaseFormFragment.FormPage.SOCIAL
            R.id.bridgeEmergencyFormFragment -> BaseFormFragment.FormPage.EMERGENCY
            else -> return
        }
        viewModel.setFieldsForNextFormPage(nextDestFormPage)
    }


    private val captureDroneCamLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.extras?.let {
            when (result.resultCode) {
                RESULT_WITHOUT_PARENT -> {

                    val parcelData = it.getDataParcel(
                        FIELD_RESULT_KEY,
                        CapturedImageParcelData.NonIncludeParent::class.java
                    )!!

                    viewModel.sendAction(
                        CheckFormUiAction.SaveCapturedImage(
                            parcelData.imageFile.absolutePath,
                            parcelData.fieldPosition
                        )
                    )

                }

                RESULT_WITH_PARENT -> {

                    val parcelData = it.getDataParcel(
                        PARENT_RESULT_KEY,
                        CapturedImageParcelData.IncludeParent::class.java
                    )!!

                    viewModel.sendAction(
                        CheckFormUiAction.SaveCapturedImage(
                            parcelData.imageFile.absolutePath,
                            parcelData.fieldPosition,
                            parcelData.parentFieldPosition
                        )
                    )

                }

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

    private fun navigate(actionId: Int) {
        navController.navigate(
            actionId
        )
    }

    override fun openGallery(
        fieldPosition: Int,
        parentFieldPosition: Int
    ) {
        Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"

            saveFieldPosition(fieldPosition, parentFieldPosition)

            val chooser = Intent.createChooser(
                this,
                getString(R.string.choose_a_picture)
            )
            intentGallery.launch(chooser)
        }
    }

    override fun openCamera(
        fieldPosition: Int,
        parentFieldPosition: Int
    ) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        saveFieldPosition(fieldPosition, parentFieldPosition)

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
            intentCamera.launch(intent)
        }
    }


    override fun openCameraDrone(fieldPosition: Int, parentFieldPosition: Int) {
        val intent = Intent(this, DroneCamCaptureActivity::class.java)
        intent.putExtra(
            DroneCamCaptureActivity.FIELD_POSITION_KEY,
            fieldPosition
        )
        if (parentFieldPosition != -1) {
            intent.putExtra(
                DroneCamCaptureActivity.PARENT_FIELD_POSITION_KEY,
                parentFieldPosition
            )
        }
        captureDroneCamLauncher
            .launch(intent)
    }

    private val intentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val lastSavePosition = fieldPositionIntentPhoto ?: return@registerForActivityResult

            val fieldPosition = lastSavePosition.fieldPosition
            val parentFieldPosition = lastSavePosition.parentFieldPosition

            if (fieldPosition == -1) return@registerForActivityResult

            currSelectedPhotoPath?.let {
                val path = it
                viewModel.sendAction(
                    CheckFormUiAction.SaveCapturedImage(
                        path,
                        fieldPosition,
                        parentFieldPosition
                    )
                )
                /*val file = File(path)
                    if (file.exists()) file.delete()*/

            }
        } else if (result.resultCode == RESULT_CANCELED) {
            result.data?.data?.let { imageUri ->
                val file = imageUri.path?.let { File(it) }
                file?.delete()
            }
        }
    }

    private val intentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val lastSavePosition = fieldPositionIntentPhoto ?: return@registerForActivityResult

            val fieldPosition = lastSavePosition.fieldPosition
            val parentFieldPosition = lastSavePosition.parentFieldPosition

            if (fieldPosition == -1) return@registerForActivityResult

            val imageUri = result.data?.data
            imageUri?.let {
                uriToFileJob?.cancel()
                uriToFileJob = lifecycleScope.launch {
                    val file = uriToFile(it, this@AddBridgeCheckFormActivity) ?: return@launch


                    viewModel.sendAction(
                        CheckFormUiAction.SaveCapturedImage(
                            file.absolutePath,
                            fieldPosition,
                            parentFieldPosition
                        )
                    )

                    /*if (file.exists()) file.delete()*/
                }

            }
        }
    }

    override fun onResume() {
        binding.viewDroneCam.startStream()
        super.onResume()
    }

    override fun onPause() {
        binding.viewDroneCam.stopStream()
        super.onPause()
    }

    override fun onStop() {
        binding.viewDroneCam.stopStream()
        uriToFileJob?.cancel()
        super.onStop()
    }

    override fun onDestroy() {
        navController.removeOnDestinationChangedListener(
            onDestinationChangedListener
        )
        super.onDestroy()

    }

    private fun saveFieldPosition(
        fieldPosition: Int,
        parentFieldPosition: Int
    ) {
        fieldPositionIntentPhoto = FieldPositionIntentPhoto(
            fieldPosition,
            parentFieldPosition
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fieldPositionIntentPhoto?.let {
            outState.putParcelable(FIELDS_SAVE_KEY, it)
        }
        currSelectedPhotoPath?.let {
            outState.putString(IMAGE_PATH_SAVE_KEY, it)
        }
        isViewModelStartingSession.let {
            outState.putBoolean(VIEW_MODEL_SESSION_SAVE_KEY, it)
        }

    }

    companion object {
        const val RESULT_WITH_PARENT = 10
        const val RESULT_WITHOUT_PARENT = 100

        const val FIELD_RESULT_KEY = "fieldd"
        const val PARENT_RESULT_KEY = "parentt"

        const val FIELDS_SAVE_KEY = "save"

        const val IMAGE_PATH_SAVE_KEY = "image"

        const val VIEW_MODEL_SESSION_SAVE_KEY = "session"

        const val ADD_MODE_KEY = "add"
        const val MODE_ID_KEY = "mode_id"
    }
}