package com.smartdrobi.aplikasipkm.data.typeconverter

import androidx.room.TypeConverter

class ListStringImagePathTypeConverter {
    @TypeConverter
    fun toListString(value: String): List<String> {
        return value.split(", ").filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun fromListString(list: List<String>): String {
        return list.joinToString(", ")
    }
}