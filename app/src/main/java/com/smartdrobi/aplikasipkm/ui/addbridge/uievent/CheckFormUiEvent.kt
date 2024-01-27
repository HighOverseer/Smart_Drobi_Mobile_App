package com.smartdrobi.aplikasipkm.ui.addbridge.uievent

import android.graphics.Bitmap
import com.smartdrobi.aplikasipkm.domain.SingleEvent
import com.smartdrobi.aplikasipkm.ui.addbridge.viewmodel.AddBridgeCheckFormViewModel

sealed class CheckFormUiEvent private constructor(
    private val isSingleEvent:Boolean
){

    private var singleEvent:SingleEvent<Unit>?=null

    init {
        if (isSingleEvent){
            singleEvent = SingleEvent(Unit)
        }
    }


    operator fun invoke(
        event:() -> Unit
    ){
        if (isSingleEvent){
            singleEvent?.getContentIfNotHandled()?.let {
                event()
            }
        }else event()
    }

    class StartingSession:CheckFormUiEvent(true)
    class NotifyWhenFragmentReadyToInit:CheckFormUiEvent(true)

    class NotifyAddedImage(
        val selectedFieldPosition:Int
    ):CheckFormUiEvent(true)

    class NotifyAddedImageOnNestedRv(
        val parentFieldPosition:Int,
        val fieldPosition:Int
    ):CheckFormUiEvent(true)

    class EndingSession(
        val resultCode:Int
    ):CheckFormUiEvent(true)

}