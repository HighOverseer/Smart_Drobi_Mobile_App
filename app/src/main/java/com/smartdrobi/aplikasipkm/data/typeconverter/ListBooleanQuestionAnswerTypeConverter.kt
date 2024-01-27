package com.smartdrobi.aplikasipkm.data.typeconverter

import androidx.room.TypeConverter
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

class ListBooleanQuestionAnswerTypeConverter {

    @TypeConverter
    fun fromListBooleanQuestionAnswer(
        list: List<BridgeCheckField.BooleanQuestionAnswer>
    ): String {
        return list.joinToString(", ") { it.toString() }
    }

    @TypeConverter
    fun toListBooleanQuestionAnswer(
        value: String
    ): List<BridgeCheckField.BooleanQuestionAnswer> {
        return value.split(", ").map {
            BridgeCheckField.BooleanQuestionAnswer.valueOf(it)
        }
    }
}