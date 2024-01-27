package com.smartdrobi.aplikasipkm.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.domain.SingleEvent
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: Repository
):ViewModel() {

    private val bridgePreviewsByHistory = repository
        .getBridgePreviewsByHistory()
        .asLiveData()

    private val _uiState = MediatorLiveData(UiState())
    val uiState:LiveData<UiState> = _uiState


    fun getLatestBridgeCheckIdInEachBridge(selectedBridgeId:Int) {
        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch {
            val bridgeCheckId = repository.getLatestBridgeCheckIdOnSelectedBridge(selectedBridgeId)
            _uiState.value = _uiState.value?.copy(
                isLoading = false,
                bridgeCheckIdForSelectedBridgeEvent = SingleEvent(bridgeCheckId)
            )
        }
    }

    init {
        _uiState.addSource(bridgePreviewsByHistory){
            _uiState.value = _uiState.value?.copy(
                listBridgePreviewsByHistory = it
            )
        }
    }

    data class UiState(
        val listBridgePreviewsByHistory:List<BridgePreview> = emptyList(),
        val isLoading:Boolean = false,
        val bridgeCheckIdForSelectedBridgeEvent:SingleEvent<Int>? = null
    )

    class ViewModelFactory (
        private val repository: Repository
    ): ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            modelClass.let out@ {
                when {
                    it.isAssignableFrom(HistoryViewModel::class.java) -> {
                        return HistoryViewModel(repository) as T
                    }
                    else -> return@out
                }
            }
            return super.create(modelClass)
        }
    }
}