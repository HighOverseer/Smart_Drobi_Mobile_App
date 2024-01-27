package com.smartdrobi.aplikasipkm.domain.model

data class BridgePreview(
    val id:Int,
    val imagePath:String,
    val name:String,
    val lastInspectionDate:String,
    val nextInspectionDate:String,
    val location:String
)

