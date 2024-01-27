package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField

@Entity(tableName = "social_page_answer")
data class SocialPageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,

    val uncleanlinessAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val uncleanlinessImagePaths:List<String>,

    val incompatibilityAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val incompatibilityImagePaths:List<String>,

    val activityAnswer: BridgeCheckField.BooleanQuestionAnswer,
    val activityImagePaths:List<String>,
)