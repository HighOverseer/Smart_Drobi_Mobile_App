package com.smartdrobi.aplikasipkm.ui.dronecam

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.smartdrobi.aplikasipkm.databinding.ActivityDroneCamCaptureBinding
import com.smartdrobi.aplikasipkm.domain.helper.saveToFile
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.dronecam.domain.CapturedImageParcelData
import com.smartdrobi.aplikasipkm.ui.dronecam.domain.OnCaptureSessionSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DroneCamCaptureActivity : AppCompatActivity(), OnCaptureSessionSuccess {

    private lateinit var binding: ActivityDroneCamCaptureBinding
    private var parentImageCollectionFieldPosition = -1
    private var imageCollectionFieldPosition = -1

    private var job: Job? = null

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDroneCamCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            isLoading = savedInstanceState.getBoolean(IS_LOADING_STATE_KEY)
        }
        setIsProgressBarVisible(isLoading)


        intent.apply {
            imageCollectionFieldPosition = getIntExtra(
                FIELD_POSITION_KEY, -1
            )

            if (imageCollectionFieldPosition == -1) finish()

            parentImageCollectionFieldPosition = getIntExtra(
                PARENT_FIELD_POSITION_KEY, -1
            )
        }
    }

    override fun saveImage(imageBitmap: Bitmap) {
        job?.cancel()

        setIsProgressBarVisible(true)
        lifecycleScope.launch {
            val intent = Intent()
            val parcelData: CapturedImageParcelData
            val file = imageBitmap.saveToFile(this@DroneCamCaptureActivity)
            val hasSaved = file.exists()

            if (!hasSaved) return@launch

            val isIncludeParent = parentImageCollectionFieldPosition != -1
            if (isIncludeParent) {
                parcelData = CapturedImageParcelData.IncludeParent(
                    parentImageCollectionFieldPosition,
                    imageCollectionFieldPosition,
                    file
                )
                intent.putExtra(
                    AddBridgeCheckFormActivity.PARENT_RESULT_KEY,
                    parcelData
                )
                setResult(
                    AddBridgeCheckFormActivity.RESULT_WITH_PARENT,
                    intent
                )
                setIsProgressBarVisible(false)
                finish()
                return@launch
            }
            parcelData = CapturedImageParcelData.NonIncludeParent(
                imageCollectionFieldPosition,
                file
            )
            intent.putExtra(
                AddBridgeCheckFormActivity.FIELD_RESULT_KEY,
                parcelData
            )
            setResult(
                AddBridgeCheckFormActivity.RESULT_WITHOUT_PARENT,
                intent
            )
            setIsProgressBarVisible(false)
            finish()
        }
    }

    private fun setIsProgressBarVisible(isVisible: Boolean) {
        isLoading = isVisible
        binding.progressBar.isVisible = isVisible
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_LOADING_STATE_KEY, isLoading)
    }

    companion object {
        const val FIELD_POSITION_KEY = "field"
        const val PARENT_FIELD_POSITION_KEY = "parent"

        const val IS_LOADING_STATE_KEY = "isLoading"
    }
}