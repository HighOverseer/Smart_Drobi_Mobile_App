package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import com.smartdrobi.aplikasipkm.domain.helper.showDialogIntentPhoto
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.BooleanQuestionsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.ImageCollectionAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.IntentPhotoInterface
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction

class BridgeConvenienceFormFragment
    : BaseFormFragment(
    FormPage.CONVENIENCE
),
    BooleanQuestionsAdapter.OnItemCallback,
    ImageCollectionAdapter.OnImageCollectionCallback {

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
                FormPage.CONVENIENCE,
                parentFieldPosition,
                fieldPosition,
                newVisibility
            )
        )
    }


    enum class ViewId(val id: Int) {
        FLOOR_SYSTEM(1),
        UPPER_BUILDING(2),
        SHORT_ROAD(3)
    }

    enum class QuestionId(val id: Int) {
        FLOOR_SYSTEM_A(1),

        UPPER_BUILDING_A(2),

        SHORT_ROAD_A(3)

    }
}