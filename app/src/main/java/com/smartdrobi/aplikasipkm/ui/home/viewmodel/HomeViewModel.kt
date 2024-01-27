package com.smartdrobi.aplikasipkm.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.domain.helper.toStateFlow
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.SearchState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: Repository
) : ViewModel() {

    private var searchJob: Job? = null
    private val _query = MutableLiveData("")

    val bridgePreviews = _query.asFlow().flatMapLatest {
        repository.searchBridgePreview(it)
    }.toStateFlow(viewModelScope, emptyList())

    /*val bridgePreviews = _query.switchMap {
        repository.searchBridgePreview(it)
    }*/

    fun searchBridgePreviews(query: String, searchState: SearchState) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            var delayTime = 500L
            if (!searchState.hasFocus) delayTime = 0L

            delay(delayTime)
            _query.value = query
        }
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