package com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form

import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.BridgeCheckFormFieldsAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction

class BridgeEmergencyFormFragment
    : BaseFormFragment(FormPage.EMERGENCY),
    BridgeCheckFormFieldsAdapter.OnHeaderBooleanQuestionCallback,
    BridgeCheckFormFieldsAdapter.OnMultiFieldCallback {

    override fun saveAnswer(fieldPosition: Int, newAnswer: BridgeCheckField.BooleanQuestionAnswer) {
        viewModel.sendAction(
            CheckFormUiAction.SaveBooleanAnswer(
                newAnswer,
                fieldPosition
            )
        )
    }


    override fun saveNewText(viewId: ElementFieldViewId, newText: String) {
        viewModel.sendAction(
            CheckFormUiAction.SaveMultifieldText(
                viewId,
                newText
            )
        )
    }

    enum class ViewId(val id: Int) {
        ACT(0),
        ELEMENT(1),
        CONDITION_INVENTORY(2),
        CONDITION_DETAIL(3)
    }

    enum class ElementFieldViewId(val id: Int) {
        CODE(0),
        DESC(1),
        APB(2),
        X(3),
        Y(4),
        Z(5),
        REASON(6)
    }
}