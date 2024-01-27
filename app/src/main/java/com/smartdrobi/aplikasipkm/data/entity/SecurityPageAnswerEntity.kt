package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

@Entity(tableName = "security_page_answer")
data class SecurityPageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val landfillAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val landFillImagePaths:List<String> = emptyList(),

    val riverFlowAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val riverFlowImagePaths:List<List<String>>,

    val foundationAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val foundationImagePaths:List<List<String>>,

    val lowerBuildingAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val lowerBuildingImagePaths:List<List<String>>,

    val upperBuildingAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val upperBuildingImagePaths:List<List<String>>,

    val siarMuaiAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val siarMuaiImagePaths:List<List<String>>,

    val placementAnswer:List<BridgeCheckField.BooleanQuestionAnswer>,
    val placementImagePaths:List<List<String>>,
)