package com.smartdrobi.aplikasipkm.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.smartdrobi.aplikasipkm.data.entity.BridgeCheckEntity
import com.smartdrobi.aplikasipkm.data.entity.ConveniencePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.EmergencyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.FirstPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.MaintenancePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SafetyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SecurityPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SocialPageAnswerEntity

data class BridgeCheckAndAnswers(
    @Embedded
    val bridgeCheckEntity: BridgeCheckEntity,

    @Relation(
        parentColumn = "firstPageAnswerId",
        entityColumn = "id"
    )
    val firstPageAnswerEntity: FirstPageAnswerEntity,

    @Relation(
        parentColumn = "securityPageAnswerId",
        entityColumn = "id"
    )
    val securityPageAnswerEntity: SecurityPageAnswerEntity,

    @Relation(
        parentColumn = "safetyPageAnswerId",
        entityColumn = "id"
    )
    val safetyPageAnswerEntity: SafetyPageAnswerEntity,

    @Relation(
        parentColumn = "conveniencePageAnswerId",
        entityColumn = "id"
    )
    val conveniencePageAnswerEntity: ConveniencePageAnswerEntity,

    @Relation(
        parentColumn = "maintenancePageAnswerId",
        entityColumn = "id"
    )
    val maintenancePageAnswerEntity: MaintenancePageAnswerEntity,

    @Relation(
        parentColumn = "socialPageAnswerId",
        entityColumn = "id"
    )
    val socialPageAnswerEntity: SocialPageAnswerEntity,

    @Relation(
        parentColumn = "emergencyPageAnswerId",
        entityColumn = "id"
    )
    val emergencyPageAnswerEntity: EmergencyPageAnswerEntity
)