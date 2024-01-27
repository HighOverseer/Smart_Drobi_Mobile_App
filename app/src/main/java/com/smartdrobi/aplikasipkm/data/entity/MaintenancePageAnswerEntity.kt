package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

@Entity(tableName = "maintenance_page_answer")
data class MaintenancePageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,

    val routineAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val routineImagePaths:List<List<String>>,

    val periodicAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val periodicImagePaths:List<List<String>>,

    val rehabilitationAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val rehabilitationImagePaths:List<List<String>>,

    val replacementAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val replacementImagePaths:List<List<String>>,

    val wideningAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val wideningImagePaths:List<String>,
)