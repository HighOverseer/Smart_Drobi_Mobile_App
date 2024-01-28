package com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheck
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getConvenienceFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getEmergencyFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getFirstFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getMaintenanceFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getSafetyFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getSecurityFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.domain.getSocialFormFields
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BaseFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.fragment.form.BridgeEmergencyFormFragment
import com.smartdrobi.aplikasipkm.ui.addbridge.uiaction.CheckFormUiAction
import com.smartdrobi.aplikasipkm.ui.addbridge.uievent.CheckFormUiEvent
import com.smartdrobi.aplikasipkm.ui.addbridge.uistate.CheckFormUiState
import com.smartdrobi.aplikasipkm.ui.home.HomeActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddBridgeCheckFormViewModel(
    private val repository: Repository,
    args: Bundle
) : ViewModel() {

    enum class Session {
        ADD,
        EDIT
    }

    private lateinit var currentSession: Session

    private lateinit var currentBridgeCheck: BridgeCheck

    private val _uiState = MutableLiveData(CheckFormUiState())
    val uiState: LiveData<CheckFormUiState> = _uiState

    private val _uiEvent = MutableLiveData<CheckFormUiEvent>()
    val uiEvent: LiveData<CheckFormUiEvent> = _uiEvent

    private val _listImagesPath = mutableListOf<String>()
    val listImagesPath: List<String> = _listImagesPath

    private fun sendEvent(event: CheckFormUiEvent) {
        try {
            _uiEvent.value = event
        } catch (e: IllegalStateException) {
            _uiEvent.postValue(event)
        }
    }

    private var job: Job? = null

    private var saveTextJob: Job? = null

    fun setFieldsForNextFormPage(
        nextDestFormPage: BaseFormFragment.FormPage,
    ) {
        //case when config changes
        val currFormPage = uiState.value?.currentFormPage ?: return

        try {
            //check if currentBridgeCheck Has been initialized
            currentBridgeCheck.id
        } catch (e: UninitializedPropertyAccessException) {
            return
        }

        if (currFormPage == nextDestFormPage) return

        job?.cancel()
        _uiState.value = CheckFormUiState()

        job = viewModelScope.launch {
            val nextDestFields = when (nextDestFormPage) {
                BaseFormFragment.FormPage.FIRST -> getFirstFormFields(currentBridgeCheck)
                BaseFormFragment.FormPage.SECURITY -> getSecurityFormFields(currentBridgeCheck)
                BaseFormFragment.FormPage.CONVENIENCE -> getConvenienceFormFields(currentBridgeCheck)
                BaseFormFragment.FormPage.SAFETY -> getSafetyFormFields(currentBridgeCheck)
                BaseFormFragment.FormPage.MAINTENANCE -> getMaintenanceFormFields(currentBridgeCheck)
                BaseFormFragment.FormPage.SOCIAL -> getSocialFormFields(currentBridgeCheck)
                BaseFormFragment.FormPage.EMERGENCY -> getEmergencyFormFields(currentBridgeCheck)
                else -> return@launch
            }
            _uiState.value = _uiState.value?.copy(
                currentFormPage = nextDestFormPage,
                currentListFields = nextDestFields
            )
        }

    }

    fun sendAction(
        action: CheckFormUiAction
    ) {
        uiState.value?.let { (_, currentListFields) ->
            when (action) {
                is CheckFormUiAction.SaveLastText -> {
                    saveTextJob?.cancel()
                    saveTextJob = viewModelScope.launch {
                        delay(250L)
                        val selectedField = currentListFields[action.fieldPosition]


                        if (selectedField !is BridgeCheckField.Text) return@launch

                        selectedField.saveNewText(action.newText)

                        selectedField.saveToBridgeCheck(currentBridgeCheck, selectedField)
                    }
                }

                is CheckFormUiAction.SaveMultifieldText -> {
                    saveTextJob?.cancel()
                    saveTextJob = viewModelScope.launch {
                        delay(250L)
                        val selectedField = currentListFields[2]

                        if (selectedField !is BridgeCheckField.MultifieldText) return@launch


                        when (action.viewId) {
                            BridgeEmergencyFormFragment.ElementFieldViewId.CODE -> {
                                selectedField.code = action.newText
                            }

                            BridgeEmergencyFormFragment.ElementFieldViewId.APB -> selectedField.apb =
                                action.newText

                            BridgeEmergencyFormFragment.ElementFieldViewId.X -> selectedField.x =
                                action.newText

                            BridgeEmergencyFormFragment.ElementFieldViewId.Y -> selectedField.y =
                                action.newText

                            BridgeEmergencyFormFragment.ElementFieldViewId.Z -> selectedField.z =
                                action.newText

                            BridgeEmergencyFormFragment.ElementFieldViewId.DESC -> selectedField.desc =
                                action.newText

                            BridgeEmergencyFormFragment.ElementFieldViewId.REASON -> selectedField.reason =
                                action.newText
                        }

                        selectedField.saveToBridgeCheck(currentBridgeCheck, selectedField)

                    }
                }

                is CheckFormUiAction.SaveBooleanAnswer -> {
                    val isFieldAChild = action.parentFieldPosition != -1

                    if (isFieldAChild) {
                        val parentField = currentListFields[action.parentFieldPosition]

                        if (parentField !is BridgeCheckField.ContainerBooleans) return

                        val selectedField = parentField.booleanQuestions[action.fieldPosition]
                        selectedField.answer = action.newAnswer

                        selectedField.saveToBridgeCheck(currentBridgeCheck, selectedField)

                    } else {
                        val selectedField = currentListFields[action.fieldPosition]

                        if (selectedField !is BridgeCheckField.BooleanField) return

                        selectedField.saveNewAnswer(action.newAnswer)

                        selectedField.saveToBridgeCheck(currentBridgeCheck, selectedField)
                    }
                }

                is CheckFormUiAction.SaveCapturedImage -> {
                    val isFieldAChild = action.parentFieldPosition != -1

                    if (isFieldAChild) {
                        val parentField = currentListFields[action.parentFieldPosition]

                        if (parentField !is BridgeCheckField.ContainerBooleans) return

                        val selectedField = parentField.booleanQuestions[action.fieldPosition]
                        selectedField.listImagePath.add(action.imagePath)

                        _listImagesPath.add(action.imagePath)

                        sendEvent(
                            CheckFormUiEvent.NotifyAddedImageOnNestedRv(
                                action.parentFieldPosition,
                                action.fieldPosition
                            )
                        )

                        selectedField.saveToBridgeCheck(currentBridgeCheck, selectedField)

                    } else {
                        val selectedField =
                            currentListFields[action.fieldPosition] as BridgeCheckField.BooleanQuestion
                        selectedField.listImagePath.add(action.imagePath)

                        _listImagesPath.add(action.imagePath)

                        sendEvent(
                            CheckFormUiEvent.NotifyAddedImage(
                                action.fieldPosition
                            )
                        )
                        selectedField.saveToBridgeCheck(currentBridgeCheck, selectedField)
                    }

                }

                is CheckFormUiAction.SaveImageCollectionsVisibility -> {
                    val currField = currentListFields[action.parentFieldPosition]

                    if (currField !is BridgeCheckField.ContainerBooleans) return

                    val currQuestion = currField.booleanQuestions[action.fieldPosition]
                    currQuestion.isImageCollectionsVisible = action.newVisibility

                }

                is CheckFormUiAction.SaveHeaderImageCollectionsVisibility -> {
                    val selectedField = currentListFields[action.fieldPosition]

                    if (selectedField !is BridgeCheckField.BooleanQuestion) return

                    selectedField.isImageCollectionsVisible = action.newVisibility
                }

                is CheckFormUiAction.FinishSession -> {
                    when (currentSession) {
                        Session.ADD -> {

                            _uiState.value = CheckFormUiState()

                            viewModelScope.launch {

                                repository.insertBridgeCheck(
                                    currentBridgeCheck
                                )


                                sendEvent(
                                    CheckFormUiEvent.EndingSession(
                                        HomeActivity.ADD_CHECK_RESULT_SUCCESS
                                    )
                                )
                            }


                        }

                        Session.EDIT -> {

                            _uiState.value = CheckFormUiState()

                            viewModelScope.launch {
                                repository.updateBridgeCheck(
                                    currentBridgeCheck
                                )

                                sendEvent(
                                    CheckFormUiEvent.EndingSession(
                                        HomeActivity.EDIT_CHECK_RESULT_SUCCESS
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    init {
        sendEvent(
            CheckFormUiEvent.StartingSession()
        )
        viewModelScope.launch {
            args.let {

                val isInAddMode = it.getBoolean(AddBridgeCheckFormActivity.ADD_MODE_KEY)
                if (isInAddMode) {
                    val selectedBridgeId = it.getInt(AddBridgeCheckFormActivity.MODE_ID_KEY)
                    currentSession = Session.ADD
                    currentBridgeCheck = BridgeCheck(bridgeId = selectedBridgeId)
                } else {
                    currentSession = Session.EDIT
                    val selectedBridgeCheckId = it.getInt(AddBridgeCheckFormActivity.MODE_ID_KEY)
                    currentBridgeCheck = repository.getBridgeCheckById(selectedBridgeCheckId)
                }
            }

            sendEvent(
                CheckFormUiEvent.NotifyWhenFragmentReadyToInit()
            )
        }

    }

    class ViewModelFactory(
        private val repository: Repository,
        private val arguments: Bundle
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            modelClass.let out@{
                when {
                    it.isAssignableFrom(AddBridgeCheckFormViewModel::class.java) -> {
                        return AddBridgeCheckFormViewModel(repository, arguments) as T
                    }

                    else -> return@out
                }
            }
            return super.create(modelClass)
        }
    }

}