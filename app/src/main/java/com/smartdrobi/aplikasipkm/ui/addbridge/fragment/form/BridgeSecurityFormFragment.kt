package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import com.smartdrobi.aplikasipkm.domain.helper.showDialogIntentPhoto
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.BooleanQuestionsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.ImageCollectionAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.IntentPhotoInterface
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction


class BridgeSecurityFormFragment
    : BaseFormFragment(FormPage.SECURITY),
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


    override fun saveImageCollectionsVisibility(
        parentFieldPosition: Int,
        fieldPosition: Int,
        newVisibility: Boolean
    ) {
        viewModel.sendAction(
            CheckFormUiAction.SaveImageCollectionsVisibility(
                FormPage.SECURITY,
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


    enum class ViewId(val id: Int) {
        LANDFILL(0),
        RIVER_FLOW(1),
        FOUNDATION(2),
        LOWER_BUILDING(3),
        UPPER_BUILDING(4),
        SIAR_MUAI(5),
        PLACEMENT(6)
    }

    enum class QuestionId(val id: Int) {
        LANDFILL_A(0),

        RIVER_FLOW_A(1),
        RIVER_FLOW_B(2),
        RIVER_FLOW_C(3),
        RIVER_FLOW_D(4),

        FOUNDATION_A(5),
        FOUNDATION_B(6),
        FOUNDATION_C(7),
        FOUNDATION_D(8),

        LOWER_BUILDING_A(9),
        LOWER_BUILDING_B(10),
        LOWER_BUILDING_C(11),
        LOWER_BUILDING_D(12),
        LOWER_BUILDING_E(13),
        LOWER_BUILDING_F(14),

        UPPER_BUILDING_A(15),
        UPPER_BUILDING_B(16),
        UPPER_BUILDING_C(17),
        UPPER_BUILDING_D(18),
        UPPER_BUILDING_E(19),
        UPPER_BUILDING_F(20),
        UPPER_BUILDING_G(21),
        UPPER_BUILDING_H(22),
        UPPER_BUILDING_I(23),
        UPPER_BUILDING_J(24),
        UPPER_BUILDING_K(25),
        UPPER_BUILDING_L(26),

        SIAR_MUAI_A(27),
        SIAR_MUAI_B(28),
        SIAR_MUAI_C(29),

        PLACEMENT_A(30),
        PLACEMENT_B(31),
        PLACEMENT_C(32),

    }
}