package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import android.content.Intent
import androidx.navigation.findNavController
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.domain.StaticString
import com.smartdrobi.aplikasipkm.domain.helper.showDialogIntentPhoto
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.BooleanQuestionsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.ImageCollectionAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.IntentPhotoInterface
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction
import com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel.AddBridgeCheckFormViewModel
import com.smartdrobi.aplikasipkm.ui.dronecam.DroneCamCaptureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BridgeSafetyFormFragment
    : BaseFormFragment(FormPage.SAFETY),
    BooleanQuestionsAdapter.OnItemCallback,
    ImageCollectionAdapter.OnImageCollectionCallback{

    override fun saveAnswerInChild(
        parentFieldPosition: Int,
        fieldPosition: Int,
        newAnswer: BridgeCheckField.BooleanQuestionAnswer
    ) {
        viewModel.sendAction(
            CheckFormUiAction.SaveBooleanAnswer(
                newAnswer,
                fieldPosition,
                parentFieldPosition
            )
        )
    }

    override fun saveImageCollectionsVisibility(
        parentFieldPosition: Int,
        fieldPosition: Int,
        newVisibility: Boolean
    ) {
        viewModel.sendAction(
            CheckFormUiAction.SaveImageCollectionsVisibility(
                FormPage.SAFETY,
                parentFieldPosition,
                fieldPosition,
                newVisibility
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


    enum class ViewId(val id:Int){
        BACKREST(0),
        SIGN(1),
        LIGHTNING_ROD(2),
        SMKS(3)
    }

    enum class QuestionId(val id:Int){
        BACKREST_A(0),
        BACKREST_B(1),
        BACKREST_C(2),

        SIGN_A(3),
        SIGN_B(4),

        LIGHTNING_ROD_A(5),
        LIGHTNING_ROD_B(6),
        LIGHTNING_ROD_C(7),
        LIGHTNING_ROD_D(8),

        SMKS_A(9),
        SMKS_B(10)
    }

}