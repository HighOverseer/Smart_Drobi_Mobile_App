package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartdrobi.aplikasipkm.domain.model.BridgeMaterial


@Entity(tableName = "bridge")
data class BridgeEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val imagePath:String,
    val name:String,
    val nationalNumber:Int,
    val cityNumber:Int,
    val tollNumber:Int,
    val buildDate:String,
    val latitudePosition:Double,
    val longitudePosition:Double,
    val mapLocName:String,
    val provinceCity:String,
    val length:Double,
    val wide:Double,
    val inspectionPlanDate:String,
    val bridgeMaterial: BridgeMaterial,
    //val lastInspectionDate:String?=null
)