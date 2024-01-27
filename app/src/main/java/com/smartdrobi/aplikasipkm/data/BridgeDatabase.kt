package com.smartdrobi.aplikasipkm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smartdrobi.aplikasipkm.data.dao.BridgeCheckDao
import com.smartdrobi.aplikasipkm.data.dao.BridgeDao
import com.smartdrobi.aplikasipkm.data.entity.BridgeCheckEntity
import com.smartdrobi.aplikasipkm.data.entity.BridgeEntity
import com.smartdrobi.aplikasipkm.data.entity.ConveniencePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.EmergencyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.FirstPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.MaintenancePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SafetyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SecurityPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SocialPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.typeconverter.BooleanQuestionAnswerTypeConverter
import com.smartdrobi.aplikasipkm.data.typeconverter.BridgeMaterialTypeConverter
import com.smartdrobi.aplikasipkm.data.typeconverter.DateStringConverter
import com.smartdrobi.aplikasipkm.data.typeconverter.ListBooleanQuestionAnswerTypeConverter
import com.smartdrobi.aplikasipkm.data.typeconverter.ListOfListStringImagePathTypeConverter
import com.smartdrobi.aplikasipkm.data.typeconverter.ListStringImagePathTypeConverter


@Database(
    entities = [
        BridgeEntity::class,
        BridgeCheckEntity::class,
        FirstPageAnswerEntity::class,
        SecurityPageAnswerEntity::class,
        SafetyPageAnswerEntity::class,
        ConveniencePageAnswerEntity::class,
        MaintenancePageAnswerEntity::class,
        SocialPageAnswerEntity::class,
        EmergencyPageAnswerEntity::class
        ],
    version = 1
)
@TypeConverters(
    BridgeMaterialTypeConverter::class,
    BooleanQuestionAnswerTypeConverter::class,
    ListStringImagePathTypeConverter::class,
    ListBooleanQuestionAnswerTypeConverter::class,
    ListOfListStringImagePathTypeConverter::class,
    DateStringConverter::class
)
abstract class BridgeDatabase: RoomDatabase(){

    abstract fun bridgeDao():BridgeDao
    abstract fun bridgeCheckDao():BridgeCheckDao
    companion object {
        @Volatile
        private var INSTANCE: BridgeDatabase?=null

        fun getDatabase(context: Context): BridgeDatabase {
            return INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    BridgeDatabase::class.java,
                    "bridge.db"
                ).build()
                return INSTANCE as BridgeDatabase
            }
        }
    }
}