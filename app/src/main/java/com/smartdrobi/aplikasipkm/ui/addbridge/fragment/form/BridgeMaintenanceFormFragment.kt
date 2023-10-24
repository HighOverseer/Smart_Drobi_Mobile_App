package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import android.content.Intent
import com.smartdrobi.aplikasipkm.domain.helper.showDialogIntentPhoto
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.BooleanQuestionsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.BridgeCheckFormFieldsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.ImageCollectionAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.IntentPhotoInterface
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction
import com.smartdrobi.aplikasipkm.ui.dronecam.DroneCamCaptureActivity

class BridgeMaintenanceFormFragment
    : BaseFormFragment(FormPage.MAINTENANCE),
    BooleanQuestionsAdapter.OnItemCallback,
    BridgeCheckFormFieldsAdapter.OnHeaderBooleanQuestionWithImagesCallback,
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

    override fun saveAnswer(fieldPosition: Int, newAnswer: BridgeCheckField.BooleanQuestionAnswer) {
        viewModel.sendAction(
            CheckFormUiAction.SaveBooleanAnswer(
                newAnswer,
                fieldPosition
            )
        )
    }

    override fun saveCollectionsVisibility(
        selectedFieldPosition: Int,
        newVisibility: Boolean
    ) {
        viewModel.sendAction(
            CheckFormUiAction.SaveHeaderImageCollectionsVisibility(
                selectedFieldPosition,
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


    override fun saveImageCollectionsVisibility(
        parentFieldPosition: Int,
        fieldPosition: Int,
        newVisibility: Boolean
    ) {
        viewModel.sendAction(
            CheckFormUiAction.SaveImageCollectionsVisibility(
                FormPage.MAINTENANCE,
                parentFieldPosition,
                fieldPosition,
                newVisibility
            )
        )
    }

    enum class ViewId(val id:Int){
        ROUTINE(0),
        PERIODIC(1),
        REHABILITATION(2),
        REPLACEMENT(3),
        WIDENING(4)
    }

    enum class QuestionId(val id:Int){
        ROUTINE_A(0),
        ROUTINE_B(1),
        ROUTINE_C(2),
        ROUTINE_D(3),

        PERIODIC_A(4),
        PERIODIC_B(5),
        PERIODIC_C(6),
        PERIODIC_D(7),
        PERIODIC_E(8),
        PERIODIC_F(9),
        PERIODIC_G(10),
        PERIODIC_H(11),
        PERIODIC_I(12),
        PERIODIC_J(13),
        PERIODIC_K(14),
        PERIODIC_L(15),
        PERIODIC_M(16),
        PERIODIC_N(17),

        REHABILITATION_A(18),
        REHABILITATION_B(19),
        REHABILITATION_C(20),

        REPLACEMENT_A(21),
        REPLACEMENT_B(22),

    }
}