package com.smartdrobi.aplikasipkm.ui.addbridge.uiaction

import android.graphics.Bitmap
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BaseFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeEmergencyFormFragment

sealed class CheckFormUiAction private constructor(
    val fieldPosition: Int
) {

    data object FinishSession:CheckFormUiAction(-1)

    class SaveLastText(
        fieldPosition: Int,
        val newText:String
    ):CheckFormUiAction(fieldPosition)

    class SaveCapturedImage(
        val imagePath: String,
        fieldPosition:Int,
        val parentFieldPosition: Int = -1//if any
    ):CheckFormUiAction(fieldPosition)

    class SaveImageCollectionsVisibility(
        val formPage: BaseFormFragment.FormPage,
        val parentFieldPosition: Int,
        fieldPosition:Int,
        val newVisibility:Boolean
    ):CheckFormUiAction(fieldPosition)

    class SaveHeaderImageCollectionsVisibility(
        fieldPosition: Int,
        val newVisibility: Boolean
    ):CheckFormUiAction(fieldPosition)

    class SaveBooleanAnswer(
        val newAnswer: BridgeCheckField.BooleanQuestionAnswer,
        fieldPosition: Int,
        val parentFieldPosition: Int = -1
    ):CheckFormUiAction(fieldPosition)

    class SaveMultifieldText(
        val viewId:BridgeEmergencyFormFragment.ElementFieldViewId,
        val newText: String
    ):CheckFormUiAction(-1) //might want to see later
}