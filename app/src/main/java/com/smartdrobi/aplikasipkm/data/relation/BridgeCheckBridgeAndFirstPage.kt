package com.smartdrobi.aplikasipkm.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.smartdrobi.aplikasipkm.data.entity.BridgeCheckEntity
import com.smartdrobi.aplikasipkm.data.entity.BridgeEntity
import com.smartdrobi.aplikasipkm.data.entity.FirstPageAnswerEntity

data class BridgeCheckBridgeAndFirstPage(
    @Embedded
    val bridgeCheckEntity: BridgeCheckEntity,

    @Relation(
        parentColumn = "bridgeId",
        entityColumn = "id"
    )
    val bridgeEntity: BridgeEntity,

    @Relation(
        parentColumn = "firstPageAnswerId",
        entityColumn = "id"
    )
    val firstPageAnswerEntity: FirstPageAnswerEntity
)