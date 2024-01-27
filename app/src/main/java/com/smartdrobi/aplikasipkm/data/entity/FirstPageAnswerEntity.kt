package com.smartdrobi.aplikasipkm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "first_page_answer")
data class FirstPageAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val inspectorName: String = "",
    val inspectionDate: Date,
    val trafficValue: String = "",
    val lhr: String = "",
    val year: String = "",
    val note: String = "",
)