package com.smartdrobi.aplikasipkm.domain.mapper

import com.smartdrobi.aplikasipkm.data.entity.BridgeEntity
import com.smartdrobi.aplikasipkm.data.relation.BridgeAndLastInspectionDate
import com.smartdrobi.aplikasipkm.domain.helper.DATE_FORMAT_PATTERN
import com.smartdrobi.aplikasipkm.domain.helper.toString
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview

object BridgeMapper {
    fun toBridge(
        bridgeAndLastInspectionDate: BridgeAndLastInspectionDate
    ):Bridge{
        return bridgeAndLastInspectionDate.run {
            Bridge(
                id,
                imagePath,
                name,
                nationalNumber,
                cityNumber,
                tollNumber,
                buildDate,
                latitudePosition,
                longitudePosition,
                mapLocName,
                provinceCity,
                length,
                wide,
                inspectionPlanDate,
                bridgeMaterial,
                lastInspectionDate?.toString(DATE_FORMAT_PATTERN)
            )
        }
    }

    fun toBridgeEntity(bridge: Bridge):BridgeEntity{
        return bridge.run {
            BridgeEntity(
                id,
                imagePath,
                name,
                nationalNumber,
                cityNumber,
                tollNumber,
                buildDate,
                latitudePosition,
                longitudePosition,
                mapLocName,
                provinceCity,
                length,
                wide,
                inspectionPlanDate,
                bridgeMaterial
            )
        }
    }

    fun toBridgePreview(bridgeAndLastInspectionDate: BridgeAndLastInspectionDate):BridgePreview{
        return bridgeAndLastInspectionDate.run {
            BridgePreview(
                id,
                imagePath,
                name,
                lastInspectionDate?.toString(DATE_FORMAT_PATTERN)?:"-",
                inspectionPlanDate,
                mapLocName
            )
        }
    }
}