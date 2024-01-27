package com.smartdrobi.aplikasipkm.ui.home.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.domain.SingleEvent
import com.smartdrobi.aplikasipkm.domain.StaticString
import com.smartdrobi.aplikasipkm.domain.StringRes
import com.smartdrobi.aplikasipkm.domain.helper.toStateFlow
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckPreview
import com.smartdrobi.aplikasipkm.ui.home.otherview.DetailFragment.Companion.SELECTED_BRIDGE_ID_KEY
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: Repository,
    arguments: Bundle
) : ViewModel() {


    lateinit var selectedBridge: StateFlow<Bridge?>
    lateinit var selectedBridgeCheckPreviews: StateFlow<List<BridgeCheckPreview>>

    private val _toastEvent = MutableLiveData<SingleEvent<StringRes>>()
    val toastEvent: LiveData<SingleEvent<StringRes>> = _toastEvent


    fun updateBridge(bridge: Bridge) {
        viewModelScope.launch {
            repository.updateBridge(bridge)
            _toastEvent.value = SingleEvent(StaticString(R.string.berhasil_disimpan))
        }
    }


    init {
        val selectedBridgeId = arguments.getInt(SELECTED_BRIDGE_ID_KEY, -1)

        if (selectedBridgeId != -1) {
            selectedBridge = repository
                .getSelectedBridgeById(selectedBridgeId)
                .toStateFlow(viewModelScope, null)

            selectedBridgeCheckPreviews = repository
                .getBridgeCheckPreviewsInFlow(selectedBridgeId)
                .toStateFlow(viewModelScope, emptyList())
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
                    it.isAssignableFrom(DetailViewModel::class.java) -> {
                        return DetailViewModel(repository, arguments) as T
                    }

                    else -> return@out
                }
            }
            return super.create(modelClass)
        }
    }

}