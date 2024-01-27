package com.smartdrobi.aplikasipkm.data

import com.smartdrobi.aplikasipkm.data.dao.BridgeCheckDao
import com.smartdrobi.aplikasipkm.data.dao.BridgeDao
import com.smartdrobi.aplikasipkm.domain.helper.Dummy
import com.smartdrobi.aplikasipkm.domain.mapper.BridgeCheckMapper
import com.smartdrobi.aplikasipkm.domain.mapper.BridgeMapper
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheck
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckPreview
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview
import com.smartdrobi.aplikasipkm.domain.model.ConveniencePageAnswer
import com.smartdrobi.aplikasipkm.domain.model.EmergencyPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.FirstPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.MaintenancePageAnswer
import com.smartdrobi.aplikasipkm.domain.model.SafetyPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.SecurityPageAnswer
import com.smartdrobi.aplikasipkm.domain.model.SocialPageAnswer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository private constructor(
    private val bridgeCheckDao: BridgeCheckDao,
    private val bridgeDao: BridgeDao
) {

    suspend fun insertBridge(bridge: Bridge) = withContext(Dispatchers.Default) {
        val newBridgeEntity = BridgeMapper.toBridgeEntity(
            bridge.copy(id = 0)
        )
        bridgeDao.insertBridge(newBridgeEntity)
    }

    suspend fun updateBridge(bridge: Bridge) = withContext(Dispatchers.Default) {
        bridgeDao.updateBridge(
            BridgeMapper.toBridgeEntity(bridge)
        )
    }

    suspend fun getBridges(): List<Bridge> = withContext(Dispatchers.Default) {
        bridgeDao.getBridgesWithLastInspectionDate().map {
            BridgeMapper.toBridge(it)
        }
    }

    /*fun getSelectedBridgebyId(bridgeId: Int):LiveData<Bridge>{
        return bridgeDao.getSelectedBridgeById(bridgeId).map {
            BridgeMapper.toBridge(it)
        }
    }*/

    fun getSelectedBridgeById(bridgeId: Int): Flow<Bridge> {
        return bridgeDao.getSelectedBridgeById(bridgeId).map {
            withContext(Dispatchers.Default) {
                BridgeMapper.toBridge(it)
            }
        }
    }

    suspend fun getBridgePreviews(): List<BridgePreview> = withContext(Dispatchers.Default) {
        bridgeDao.getBridgesWithLastInspectionDate().map {
            BridgeMapper.toBridgePreview(it)
        }
    }

    /*    fun getBridgePreviewsLiveData():LiveData<List<BridgePreview>>{
            return bridgeDao.getBridgesWithLastInspectionDateLiveData()
                .map { list ->
                    val listWithHeader = mutableListOf(
                        BridgePreview(
                            -1,
                            "",
                            Dummy.droneCamStatus,
                            "",
                            "",
                            "")
                    )
                    list.forEach{
                        listWithHeader.add(BridgeMapper.toBridgePreview(it))
                    }
                    listWithHeader
                }
        }*/

    /*fun getBridgePreviewsByHistory():LiveData<List<BridgePreview>>{
        return bridgeDao.searchBridgePreview(query = "")
            .map { list ->
                val listBridgePreviewByHistory = mutableListOf(
                    BridgePreview(
                        -1,
                        "",
                        "",
                        "",
                        "",
                        ""
                    )
                )
                list.sortedByDescending {
                    it.lastInspectionDate
                }.forEach{
                    listBridgePreviewByHistory.add(BridgeMapper.toBridgePreview(it))
                }
                listBridgePreviewByHistory

        }

    }*/

    fun getBridgePreviewsByHistory(): Flow<List<BridgePreview>> {
        return bridgeDao.getBridgePreviewByHistory()
            .map { list ->
                withContext(Dispatchers.Default) {
                    val listBridgePreviewByHistory = mutableListOf(
                        BridgePreview(
                            -1,
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                    list.sortedByDescending {
                        it.lastInspectionDate
                    }.forEach {
                        listBridgePreviewByHistory.add(BridgeMapper.toBridgePreview(it))
                    }
                    listBridgePreviewByHistory
                }
            }

    }

    /*fun searchBridgePreview(query: String = ""): LiveData<List<BridgePreview>> {
        return bridgeDao.searchBridgePreview(query)
            .map { list ->
                val listWithHeader = mutableListOf(
                    BridgePreview(
                        -1,
                        "",
                        Dummy.droneCamStatus,
                        "",
                        "",
                        "")
                )
                list.forEach{
                    listWithHeader.add(BridgeMapper.toBridgePreview(it))
                }
                listWithHeader
            }
    }*/

    fun searchBridgePreview(query: String = ""): Flow<List<BridgePreview>> {
        return bridgeDao.searchBridgePreview(query)
            .map { list ->
                withContext(Dispatchers.Default) {
                    val listWithHeader = mutableListOf(
                        BridgePreview(
                            -1,
                            "",
                            Dummy.droneCamStatus,
                            "",
                            "",
                            ""
                        )
                    )
                    list.forEach {
                        listWithHeader.add(BridgeMapper.toBridgePreview(it))
                    }
                    listWithHeader
                }
            }
    }

    suspend fun getBridgeCheckById(bridgeCheckId: Int): BridgeCheck =
        withContext(Dispatchers.Default) {
            val bridgeCheckAndAnswer = bridgeCheckDao.getBridgeCheckAndAnswersByBridgeCheckId(
                bridgeCheckId
            )
            BridgeCheckMapper.toBridgeCheck(bridgeCheckAndAnswer)
        }

    /*fun getBridgeCheckPreviewsInLiveData(bridgeId: Int): LiveData<List<BridgeCheckPreview>> {
        return bridgeCheckDao.getBridgeCheckPreviewLiveData(bridgeId).map { list ->
            list.map {
                BridgeCheckMapper.toBridgeCheckPreview(it)
            }
        }
    }*/

    fun getBridgeCheckPreviewsInFlow(bridgeId: Int): Flow<List<BridgeCheckPreview>> {
        val flow = bridgeCheckDao.getBridgeCheckPreviewInFlow(bridgeId)
        return flow.map { list ->
            withContext(Dispatchers.Default) {
                list.map {
                    BridgeCheckMapper.toBridgeCheckPreview(it)
                }
            }

        }
    }

    suspend fun getLatestBridgeCheckIdOnSelectedBridge(selectedBridgeId: Int): Int =
        withContext(Dispatchers.Default) {
            bridgeCheckDao.getBridgeCheckPreview(selectedBridgeId)
                .sortedByDescending {
                    it.firstPageAnswerEntity.inspectionDate
                }[0]
                .bridgeCheckEntity
                .id
                .toInt()
        }

    suspend fun insertBridgeCheck(bridgeCheck: BridgeCheck): Unit =
        withContext(Dispatchers.Default) {
            bridgeCheck.apply {
                BridgeCheckMapper.apply {
                    val firstPageAnswerId = async { insertFirstPageAnswer(firstPageAnswer) }
                    val securityPageAnswerId =
                        async { insertSecurityPageAnswer(securityPageAnswer) }
                    val safetyPageAnswerId = async { insertSafetyPageAnswer(safetyPageAnswer) }
                    val maintenancePageAnswerId =
                        async { insertMaintenancePageAnswer(maintenancePageAnswer) }
                    val socialPageAnswerId = async { insertSocialPageAnswer(socialPageAnswer) }
                    val conveniencePageAnswerId =
                        async { insertConveniencePageAnswer(conveniencePageAnswer) }
                    val emergencyPageAnswerId =
                        async { insertEmergencyPageAnswer(emergencyPageAnswer) }

                    bridgeCheckDao.insertBridgeCheck(
                        toBridgeCheckEntity(
                            bridgeId,
                            firstPageAnswerId.await(),
                            safetyPageAnswerId.await(),
                            securityPageAnswerId.await(),
                            maintenancePageAnswerId.await(),
                            conveniencePageAnswerId.await(),
                            socialPageAnswerId.await(),
                            emergencyPageAnswerId.await()
                        )
                    )
                }

            }
        }

    suspend fun updateBridgeCheck(bridgeCheck: BridgeCheck): Unit =
        withContext(Dispatchers.Default) {
            bridgeCheck.apply {
                BridgeCheckMapper.apply {
                    val selectedBridgeCheckEntity =
                        bridgeCheckDao.getBridgeCheckAndAnswersByBridgeCheckId(id)

                    listOf(
                        launch {
                            updateFirstPageAnswer(
                                firstPageAnswer,
                                selectedBridgeCheckEntity.firstPageAnswerEntity.id
                            )
                        },
                        launch {
                            updateSecurityPageAnswer(
                                securityPageAnswer,
                                selectedBridgeCheckEntity.securityPageAnswerEntity.id
                            )
                        },
                        launch {
                            updateSafetyPageAnswer(
                                safetyPageAnswer,
                                selectedBridgeCheckEntity.safetyPageAnswerEntity.id
                            )
                        },
                        launch {
                            updateMaintenancePageAnswer(
                                maintenancePageAnswer,
                                selectedBridgeCheckEntity.maintenancePageAnswerEntity.id
                            )
                        },
                        launch {
                            updateSocialPageAnswer(
                                socialPageAnswer,
                                selectedBridgeCheckEntity.socialPageAnswerEntity.id
                            )
                        },
                        launch {
                            updateConveniencePageAnswer(
                                conveniencePageAnswer,
                                selectedBridgeCheckEntity.conveniencePageAnswerEntity.id
                            )
                        },
                        launch {
                            updateEmergencyPageAnswer(
                                emergencyPageAnswer,
                                selectedBridgeCheckEntity.emergencyPageAnswerEntity.id
                            )
                        }
                    ).joinAll()

                    bridgeCheckDao.updateBridgeCheck(
                        toBridgeCheckEntity(
                            bridgeId = bridgeId,
                            firstPageAnswerId = selectedBridgeCheckEntity.firstPageAnswerEntity.id,
                            securityPageAnswerId = selectedBridgeCheckEntity.securityPageAnswerEntity.id,
                            safetyPageAnswerId = selectedBridgeCheckEntity.safetyPageAnswerEntity.id,
                            maintenancePageAnswerId = selectedBridgeCheckEntity.maintenancePageAnswerEntity.id,
                            socialPageAnswerId = selectedBridgeCheckEntity.socialPageAnswerEntity.id,
                            conveniencePageAnswerId = selectedBridgeCheckEntity.conveniencePageAnswerEntity.id,
                            emergencyPageAnswerId = selectedBridgeCheckEntity.emergencyPageAnswerEntity.id
                        )
                    )
                }

            }
        }

    private suspend fun insertFirstPageAnswer(firstPageAnswer: FirstPageAnswer): Long {
        return bridgeCheckDao.insertFirstPageAnswer(
            BridgeCheckMapper.toFirstPageAnswerEntity(
                firstPageAnswer = firstPageAnswer
            )
        )
    }


    private suspend fun insertSecurityPageAnswer(securityPageAnswer: SecurityPageAnswer): Long {
        return bridgeCheckDao.insertSecurityPageAnswer(
            BridgeCheckMapper.toSecurityPageAnswerEntity(
                securityPageAnswer = securityPageAnswer
            )
        )
    }


    private suspend fun insertSafetyPageAnswer(safetyPageAnswer: SafetyPageAnswer): Long {
        return bridgeCheckDao.insertSafetyPageAnswer(
            BridgeCheckMapper.toSafetyPageAnswerEntity(
                safetyPageAnswer = safetyPageAnswer
            )
        )
    }

    private suspend fun insertMaintenancePageAnswer(maintenancePageAnswer: MaintenancePageAnswer): Long {
        return bridgeCheckDao.insertMaintenancePageAnswer(
            BridgeCheckMapper.toMaintenancePageAnswerEntity(
                maintenancePageAnswer = maintenancePageAnswer
            )
        )
    }

    private suspend fun insertSocialPageAnswer(socialPageAnswer: SocialPageAnswer): Long {
        return bridgeCheckDao.insertSocialPageAnswer(
            BridgeCheckMapper.toSocialPageAnswerEntity(
                socialPageAnswer = socialPageAnswer
            )
        )
    }

    private suspend fun insertConveniencePageAnswer(conveniencePageAnswer: ConveniencePageAnswer): Long {
        return bridgeCheckDao.insertConveniencePageAnswer(
            BridgeCheckMapper.toConveniencePageAnswerEntity(
                conveniencePageAnswer = conveniencePageAnswer
            )
        )
    }

    private suspend fun insertEmergencyPageAnswer(emergencyPageAnswer: EmergencyPageAnswer): Long {
        return bridgeCheckDao.insertEmergencyPageAnswer(
            BridgeCheckMapper.toEmergencyPageAnswerEntity(
                emergencyPageAnswer = emergencyPageAnswer
            )
        )
    }

    private suspend fun updateFirstPageAnswer(
        firstPageAnswer: FirstPageAnswer,
        firstPageAnswerId: Long
    ) {
        bridgeCheckDao.updateFirstPageAnswer(
            BridgeCheckMapper.toFirstPageAnswerEntity(
                firstPageAnswerId,
                firstPageAnswer = firstPageAnswer
            )
        )
    }


    private suspend fun updateSecurityPageAnswer(
        securityPageAnswer: SecurityPageAnswer,
        securityPageAnswerId: Long
    ) {
        bridgeCheckDao.updateSecurityPageAnswer(
            BridgeCheckMapper.toSecurityPageAnswerEntity(
                securityPageAnswerId,
                securityPageAnswer = securityPageAnswer
            )
        )
    }


    private suspend fun updateSafetyPageAnswer(
        safetyPageAnswer: SafetyPageAnswer,
        safetyPageAnswerId: Long
    ) {
        bridgeCheckDao.updateSafetyPageAnswer(
            BridgeCheckMapper.toSafetyPageAnswerEntity(
                safetyPageAnswerId,
                safetyPageAnswer = safetyPageAnswer
            )
        )
    }

    private suspend fun updateMaintenancePageAnswer(
        maintenancePageAnswer: MaintenancePageAnswer,
        maintenancePageAnswerId: Long
    ) {
        bridgeCheckDao.updateMaintenancePageAnswer(
            BridgeCheckMapper.toMaintenancePageAnswerEntity(
                maintenancePageAnswerId,
                maintenancePageAnswer = maintenancePageAnswer
            )
        )
    }

    private suspend fun updateSocialPageAnswer(
        socialPageAnswer: SocialPageAnswer,
        socialPageAnswerId: Long
    ) {
        bridgeCheckDao.updateSocialPageAnswer(
            BridgeCheckMapper.toSocialPageAnswerEntity(
                socialPageAnswerId,
                socialPageAnswer = socialPageAnswer
            )
        )
    }

    private suspend fun updateConveniencePageAnswer(
        conveniencePageAnswer: ConveniencePageAnswer,
        conveniencePageAnswerId: Long
    ) {
        bridgeCheckDao.updateConveniencePageAnswer(
            BridgeCheckMapper.toConveniencePageAnswerEntity(
                conveniencePageAnswerId,
                conveniencePageAnswer = conveniencePageAnswer
            )
        )
    }

    private suspend fun updateEmergencyPageAnswer(
        emergencyPageAnswer: EmergencyPageAnswer,
        emergencyPageAnswerId: Long
    ) {
        bridgeCheckDao.updateEmergencyPageAnswer(
            BridgeCheckMapper.toEmergencyPageAnswerEntity(
                emergencyPageAnswerId,
                emergencyPageAnswer = emergencyPageAnswer
            )
        )
    }


    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            bridgeCheckDao: BridgeCheckDao,
            bridgeDao: BridgeDao
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(
                    bridgeCheckDao, bridgeDao
                )
            }.also { INSTANCE = it }
        }
    }
}