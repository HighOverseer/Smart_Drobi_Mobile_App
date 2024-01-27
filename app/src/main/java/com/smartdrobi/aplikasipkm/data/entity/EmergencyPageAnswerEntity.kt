package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

@Entity(tableName = "emergency_page_answer")
data class EmergencyPageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val actAnswer: BridgeCheckField.BooleanQuestionAnswer,

    val elementCode:String,
    val elementDesc:String,
    val elementApb:String,
    val elementX:String,
    val elementY:String,
    val elementZ:String,
    val elementReason:String,

    val conditionInventoryAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val conditionDetailAnswer: BridgeCheckField.BooleanQuestionAnswer,
)