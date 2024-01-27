package com.smartdrobi.aplikasipkm.data.typeconverter

import androidx.room.TypeConverter
import com.smartdrobi.aplikasipkm.domain.model.BridgeMaterial

class BridgeMaterialTypeConverter {
    @TypeConverter
    fun toBridgeMaterial(value:String):BridgeMaterial{
        return BridgeMaterial.valueOf(value)
    }

    @TypeConverter
    fun fromBridgeMaterial(bridgeMaterial: BridgeMaterial):String{
        return bridgeMaterial.toString()
    }
}