package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.domain.model.MaintenancePageAnswer

@Entity(tableName = "bridge_check")
data class BridgeCheckEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val bridgeId:Int,
    val firstPageAnswerId:Long,
    val securityPageAnswerId:Long,
    val safetyPageAnswerId:Long,
    val conveniencePageAnswerId:Long,
    val maintenancePageAnswerId:Long,
    val socialPageAnswerId:Long,
    val emergencyPageAnswerId:Long
)