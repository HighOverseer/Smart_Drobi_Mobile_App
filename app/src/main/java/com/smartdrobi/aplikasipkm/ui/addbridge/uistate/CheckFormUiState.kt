package com.smartdrobi.aplikasipkm.ui.addbridge.uistate

import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BaseFormFragment

data class CheckFormUiState(
    val currentFormPage: BaseFormFragment.FormPage = BaseFormFragment.FormPage.NONE,
    val currentListFields: List<BridgeCheckField> = emptyList()
)