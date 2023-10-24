package com.smartdrobi.aplikasipkm.domain.model


private var currentIncrementId = 100
    get(){
        field += 1
        return field
    }
data class Bridge(
    val id:Int = currentIncrementId,
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
    val bridgeMaterial:BridgeMaterial,
    val lastInspectionDate:String?=null
){
    fun toBridgePreview():BridgePreview{
        return BridgePreview(
            id,
            imagePath,
            name,
            lastInspectionDate?:"-",
            inspectionPlanDate,
            mapLocName
        )
    }
}

enum class BridgeMaterial(val stringVal:String){
    Wood("Kayu"),
    Iron("Besi"),
    Steel("Baja")
}