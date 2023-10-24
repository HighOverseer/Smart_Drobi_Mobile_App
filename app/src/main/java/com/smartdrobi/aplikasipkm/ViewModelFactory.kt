package com.smartdrobi.aplikasipkm

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel.AddBridgeCheckFormViewModel

class ViewModelFactory private constructor(): ViewModelProvider.NewInstanceFactory() {

    private var args:Bundle? = null

    fun setArguments(args: Bundle?){
        this.args = args
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        modelClass.let out@ {
            when {
                it.isAssignableFrom(AddBridgeCheckFormViewModel::class.java) -> {
                    val currArgs = args ?: return@out

                    return AddBridgeCheckFormViewModel(currArgs) as T
                }
                else -> return@out

            }
        }
        args = null
        return super.create(modelClass)
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory?=null

        fun getInstance(
            args:Bundle? = null
        ): ViewModelFactory {
            return INSTANCE?.also {
                it.setArguments(args)
            } ?: synchronized(ViewModelFactory::class.java){
                INSTANCE ?: ViewModelFactory().also {
                    it.setArguments(args)
                }
            }.also {
                INSTANCE = it
            }
        }
    }
}