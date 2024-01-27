package com.smartdrobi.aplikasipkm.data.typeconverter

import androidx.room.TypeConverter

class ListOfListStringImagePathTypeConverter {
    @TypeConverter
    fun fromListOfListStringImagePath(list: List<List<String>>): String {
        return list.joinToString("; ") { "(${it.joinToString(", ")})" }
    }

    @TypeConverter
    fun toListOfListStringImagePath(value: String): List<List<String>> {
        return value.split("; ")
            .map {
                it.removeSurrounding(
                    prefix = "(",
                    suffix = ")"
                ).split(", ")
                    .filter { str -> str.isNotBlank() }
            }
    }
}