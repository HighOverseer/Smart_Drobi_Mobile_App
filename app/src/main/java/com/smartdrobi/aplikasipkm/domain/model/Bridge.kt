package com.smartdrobi.aplikasipkm.domain.model


data class Bridge(
    val id: Int = 0,
    val imagePath: String,
    val name: String,
    val nationalNumber: Int,
    val cityNumber: Int,
    val tollNumber: Int,
    val buildDate: String,
    val latitudePosition: Double,
    val longitudePosition: Double,
    val mapLocName: String,
    val provinceCity: String,
    val length: Double,
    val wide: Double,
    val inspectionPlanDate: String,
    val bridgeMaterial: BridgeMaterial,
    val lastInspectionDate: String? = null
)

enum class BridgeMaterial(val stringVal: String) {
    Wood("Kayu"),
    Iron("Besi"),
    Steel("Baja")
}