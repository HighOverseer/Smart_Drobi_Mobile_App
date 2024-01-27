package com.smartdrobi.aplikasipkm.data.typeconverter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

class BooleanQuestionAnswerTypeConverter {
    @TypeConverter
    fun toBridgeQuestionAnswer(value:String):BridgeCheckField.BooleanQuestionAnswer{
        return BridgeCheckField.BooleanQuestionAnswer.valueOf(value)
    }

    @TypeConverter
    fun fromBridgeQuestionAnswer(answer:BridgeCheckField.BooleanQuestionAnswer):String{
        return answer.toString()
    }

}