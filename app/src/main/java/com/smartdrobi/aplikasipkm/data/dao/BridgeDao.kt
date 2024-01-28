package com.smartdrobi.aplikasipkm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smartdrobi.aplikasipkm.data.entity.BridgeEntity
import com.smartdrobi.aplikasipkm.data.relation.BridgeAndLastInspectionDate
import kotlinx.coroutines.flow.Flow

@Dao
interface BridgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBridge(bridgeEntity: BridgeEntity)

    @Query(
        """
        SELECT b.*, 
        (
            SELECT inspectionDate FROM bridge_check bc 
            INNER JOIN first_page_answer f ON bc.firstPageAnswerId = f.id 
            WHERE bc.bridgeId = b.id ORDER BY f.inspectionDate DESC LIMIT 1
        ) AS lastInspectionDate FROM bridge b WHERE b.name LIKE '%' || :query || '%'
        """
    )
    fun searchBridgePreview(query: String): Flow<List<BridgeAndLastInspectionDate>>

    @Query(
        """
        SELECT b.*, 
        (
            SELECT inspectionDate FROM bridge_check bc 
            INNER JOIN first_page_answer f ON bc.firstPageAnswerId = f.id 
            WHERE bc.bridgeId = b.id ORDER BY f.inspectionDate DESC LIMIT 1
        ) AS lastInspectionDate FROM bridge b WHERE lastInspectionDate NOT NULL
        """
    )
    fun getBridgePreviewByHistory(): Flow<List<BridgeAndLastInspectionDate>>

    @Query(
        """
        SELECT b.*, 
        (
            SELECT inspectionDate FROM bridge_check bc 
            INNER JOIN first_page_answer f ON bc.firstPageAnswerId = f.id 
            WHERE bc.bridgeId = b.id ORDER BY f.inspectionDate DESC LIMIT 1
        ) AS lastInspectionDate FROM bridge b WHERE b.id = :bridgeId 
        """
    )
    fun getSelectedBridgeById(bridgeId: Int): Flow<BridgeAndLastInspectionDate>

    @Update
    suspend fun updateBridge(bridgeEntity: BridgeEntity)
}