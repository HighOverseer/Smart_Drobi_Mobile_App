package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

@Entity(tableName = "safety_page_answer")
data class SafetyPageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val backRestAnswer: List<BridgeCheckField.BooleanQuestionAnswer>,
    val backRestImagePaths: List<List<String>>,
    val signAnswer: List<BridgeCheckField.BooleanQuestionAnswer>,
    val signImagePaths: List<List<String>>,
    val lightningRodAnswer: List<BridgeCheckField.BooleanQuestionAnswer>,
    val lightningRodImagePaths: List<List<String>>,
    val smksAnswer: List<BridgeCheckField.BooleanQuestionAnswer>,
    val smksImagePaths: List<List<String>>
)