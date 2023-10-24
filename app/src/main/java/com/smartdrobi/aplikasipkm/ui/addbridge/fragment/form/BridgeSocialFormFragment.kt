package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import android.content.Intent
import com.smartdrobi.aplikasipkm.domain.helper.showDialogIntentPhoto
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.BridgeCheckFormFieldsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.ImageCollectionAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.IntentPhotoInterface
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction
import com.smartdrobi.aplikasipkm.ui.dronecam.DroneCamCaptureActivity

class BridgeSocialFormFragment
    : BaseFormFragment(FormPage.SOCIAL),
    BridgeCheckFormFieldsAdapter.OnHeaderBooleanQuestionWithImagesCallback,
    ImageCollectionAdapter.OnImageCollectionCallback {

    override fun saveAnswer(fieldPosition: Int, newAnswer: BridgeCheckField.BooleanQuestionAnswer) {
        viewModel.sendAction(
            CheckFormUiAction.SaveBooleanAnswer(
                newAnswer,
                fieldPosition
            )
        )
    }

    override fun showDialogImageChooserInChild(parentFieldPosition: Int, fieldPosition: Int) {
        val fragmentActivity = requireActivity()
        fragmentActivity.apply {
            if (this !is IntentPhotoInterface) return

            showDialogIntentPhoto(
                fragmentActivity,
                ::openGallery,
                ::openCameraDrone,
                ::openCamera,
                fieldPosition,
                parentFieldPosition
            )
        }
    }

    override fun showDialogImageChooser(fieldPosition: Int) {
        val fragmentActivity = requireActivity()
        fragmentActivity.apply {
            if (this !is IntentPhotoInterface) return

            showDialogIntentPhoto(
                fragmentActivity,
                ::openGallery,
                ::openCameraDrone,
                ::openCamera,
                fieldPosition,
                -1
            )
        }
    }

    override fun saveCollectionsVisibility(selectedFieldPosition: Int, newVisibility: Boolean) {
        viewModel.sendAction(
            CheckFormUiAction.SaveHeaderImageCollectionsVisibility(
                selectedFieldPosition,
                newVisibility
            )
        )
    }

    enum class ViewId(val id:Int){
            UNCLEANLINESS(0),
            INCOMPATIBILITY(1),
            ACTIVITY(2)
    }
}