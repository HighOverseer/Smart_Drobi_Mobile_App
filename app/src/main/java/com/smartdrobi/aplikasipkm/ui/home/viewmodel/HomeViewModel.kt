package com.smartdrobi.aplikasipkm.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.domain.helper.toStateFlow
import com.smartdrobi.aplikasipkm.domain.model.ListItem
import com.smartdrobi.aplikasipkm.domain.model.SearchHeaderBridgePreview
import com.smartdrobi.aplikasipkm.ui.home.DroneCamConnectivityStatus
import com.smartdrobi.aplikasipkm.domain.model.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

    private var searchJob: Job? = null
    private var connectDroneCamJob:Job?=null
    //private val _query = MutableLiveData("")

    private val _searchHeaderBridgePreview = MutableStateFlow(SearchHeaderBridgePreview())

    private val bridgePreviews = _searchHeaderBridgePreview.flatMapLatest {
        var delayTime = 400L
        if (!it.searchState.hasFocus) delayTime = 0L

        delay(delayTime)

        repository.searchBridgePreview(it.searchState.query)

    }.toStateFlow(viewModelScope, emptyList())

    val bridgePreviewsWithSearchHeader = combine(_searchHeaderBridgePreview, bridgePreviews){ sValue, bValue ->
        withContext(Dispatchers.Default){
            mutableListOf<ListItem>(sValue).apply { addAll(bValue) }
        }
    }.toStateFlow(viewModelScope, emptyList())

    /*val bridgePreviews = _query.switchMap {
        repository.searchBridgePreview(it)
    }*/

    fun connectDroneCam(ipAdress:String){
        connectDroneCamJob?.cancel()
        connectDroneCamJob = viewModelScope.launch {
            _searchHeaderBridgePreview.value = _searchHeaderBridgePreview.value.copy(
                droneCamConnectivityStatus = DroneCamConnectivityStatus.CONNECTING
            )

            delay(1500L)

            _searchHeaderBridgePreview.value = _searchHeaderBridgePreview.value.copy(
                droneCamConnectivityStatus = DroneCamConnectivityStatus.CONNECTED
            )

        }
    }

    fun searchBridgePreviews(searchState: SearchState) {
        if (searchState == _searchHeaderBridgePreview.value.searchState) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchHeaderBridgePreview.value = _searchHeaderBridgePreview.value.copy(
                searchState = searchState,
            )
        }

        /*searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (searchState.query.length < _searchHeaderBridgePreview.value.searchState.query.length){
                delay(100L)
            }
            _searchHeaderBridgePreview.value = _searchHeaderBridgePreview.value.copy(
                searchState = searchState,
            )
        }*/

        /*searchJob?.cancel()

        searchJob = viewModelScope.launch {
            var delayTime = 500L
            if (!searchState.hasFocus) delayTime = 0L

            delay(delayTime)

            _searchHeaderBridgePreview.value = _searchHeaderBridgePreview.value?.copy(
                searchQuery = query
            )
        }*/
    }


    class ViewModelFactory(
        private val repository: Repository
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            modelClass.let out@{
                when {
                    it.isAssignableFrom(HomeViewModel::class.java) -> {
                        return HomeViewModel(repository) as T
                    }

                    else -> return@out
                }
            }
            return super.create(modelClass)
        }
    }
}