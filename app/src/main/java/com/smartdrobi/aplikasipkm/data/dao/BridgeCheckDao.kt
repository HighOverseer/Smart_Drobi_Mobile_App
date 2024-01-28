package com.smartdrobi.aplikasipkm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.smartdrobi.aplikasipkm.data.entity.BridgeCheckEntity
import com.smartdrobi.aplikasipkm.data.entity.ConveniencePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.EmergencyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.FirstPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.MaintenancePageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SafetyPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SecurityPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.entity.SocialPageAnswerEntity
import com.smartdrobi.aplikasipkm.data.relation.BridgeCheckAndAnswers
import com.smartdrobi.aplikasipkm.data.relation.BridgeCheckBridgeAndFirstPage
import kotlinx.coroutines.flow.Flow

@Dao
interface BridgeCheckDao {
    @Query("SELECT * FROM bridge_check WHERE id = :bridgeCheckId")
    suspend fun getBridgeCheckAndAnswersByBridgeCheckId(
        bridgeCheckId: Int
    ): BridgeCheckAndAnswers

    @Transaction
    @Query("SELECT * FROM bridge_check WHERE bridgeId = :bridgeId")
    fun getBridgeCheckPreviewInFlow(bridgeId: Int): Flow<List<BridgeCheckBridgeAndFirstPage>>

    @Query("SELECT * FROM bridge_check WHERE bridgeId = :bridgeId")
    suspend fun getBridgeCheckPreview(bridgeId: Int): List<BridgeCheckBridgeAndFirstPage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBridgeCheck(bridgeCheckEntity: BridgeCheckEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstPageAnswer(firstPageAnswerEntity: FirstPageAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSecurityPageAnswer(securityPageAnswerEntity: SecurityPageAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSafetyPageAnswer(safetyPageAnswerEntity: SafetyPageAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenancePageAnswer(maintenancePageAnswerEntity: MaintenancePageAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialPageAnswer(socialPageAnswerEntity: SocialPageAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConveniencePageAnswer(conveniencePageAnswerEntity: ConveniencePageAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyPageAnswer(emergencyPageAnswerEntity: EmergencyPageAnswerEntity): Long

    @Update
    suspend fun updateBridgeCheck(bridgeCheckEntity: BridgeCheckEntity)

    @Update
    suspend fun updateFirstPageAnswer(firstPageAnswerEntity: FirstPageAnswerEntity)

    @Update
    suspend fun updateSecurityPageAnswer(securityPageAnswerEntity: SecurityPageAnswerEntity)

    @Update
    suspend fun updateSafetyPageAnswer(safetyPageAnswerEntity: SafetyPageAnswerEntity)

    @Update
    suspend fun updateMaintenancePageAnswer(maintenancePageAnswerEntity: MaintenancePageAnswerEntity)

    @Update
    suspend fun updateSocialPageAnswer(socialPageAnswerEntity: SocialPageAnswerEntity)

    @Update
    suspend fun updateConveniencePageAnswer(conveniencePageAnswerEntity: ConveniencePageAnswerEntity)

    @Update
    suspend fun updateEmergencyPageAnswer(emergencyPageAnswerEntity: EmergencyPageAnswerEntity)
}

