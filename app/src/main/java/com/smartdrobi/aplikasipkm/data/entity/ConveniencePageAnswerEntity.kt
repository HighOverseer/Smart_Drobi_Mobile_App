package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

@Entity(tableName = "convenience_page_answer")
data class ConveniencePageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val floorSystemAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val floorSystemImagePaths:List<String>,

    val upperBuildingAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val upperBuildingImagePaths:List<String>,

    val shortRoadAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val shortRoadImagePaths:List<String>,
)