package com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smartdrobi.aplikasipkm.data.Repository
import com.smartdrobi.aplikasipkm.di.Injection
import com.smartdrobi.aplikasipkm.domain.SingleEvent
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.ui.home.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBridgeFormViewModel(
    private val repository: Repository
):ViewModel() {

    private val _bridgeAddedEvent = MutableLiveData<SingleEvent<Unit>>()
    val bridgeAddedEvent:LiveData<SingleEvent<Unit>> = _bridgeAddedEvent

    private val _isLoading = MutableLiveData(false)
    val isLoading:LiveData<Boolean> = _isLoading

    fun insertBridge(bridge: Bridge){
        _isLoading.value = true
        viewModelScope.launch{
            repository.insertBridge(bridge)
            _isLoading.value = false
            _bridgeAddedEvent.value = SingleEvent(Unit)
        }
    }


    class ViewModelFactory(
        private val repository: Repository
    ): ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            modelClass.let out@{
                when {
                    it.isAssignableFrom(AddBridgeFormViewModel::class.java) -> {
                        return AddBridgeFormViewModel(repository) as T
                    }
                    else -> return@out
                }
            }
            return super.create(modelClass)
        }
    }
}