package com.smartdrobi.aplikasipkm.domain.helper

import android.net.Uri
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheck
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckPreview
import com.smartdrobi.aplikasipkm.domain.model.BridgeMaterial
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview
import com.smartdrobi.aplikasipkm.ui.home.DroneCamConnectivityStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val a =
    "/storage/emulated/0/Android/data/com.smartdrobi.aplikasipkm/files/Pictures/photo_18305932342310678567.jpg"

object Dummy {

    //fake data for temporal
    val listBridges = mutableListOf(
        Bridge(
            id = 0,
            imagePath = Uri.parse("android.resource://${R::class.java.`package`?.name}/${R.drawable.jembatan_siti_nurbaya}")
                .toString(),
            name = "Jembatan Siti Nurbaya",
            nationalNumber = 11,
            cityNumber = 122,
            tollNumber = 222,
            buildDate = "12/01/2021",
            latitudePosition = -6.2293796,
            longitudePosition = 106.6647046,
            mapLocName = "Taplau, Padang, Sumatera Barat",
            provinceCity = "Padang, Sumatera Barat",
            length = 221.3,
            wide = 11.4,
            inspectionPlanDate = "28/10/2023",
            bridgeMaterial = BridgeMaterial.Iron
        ),
        Bridge(
            id = 1,
            imagePath = Uri.parse("android.resource://${R::class.java.`package`?.name}/${R.drawable.ampera}")
                .toString(),
            name = "Jembatan Ampera",
            nationalNumber = 11,
            cityNumber = 122,
            tollNumber = 222,
            buildDate = "12/01/2021",
            latitudePosition = -2.9917713,
            longitudePosition = 104.7624691,
            mapLocName = "Jembatan Ampera, Palembang, Sumatera Selatan",
            provinceCity = "Palembang, Sumatera Selatan",
            length = 221.3,
            wide = 11.4,
            inspectionPlanDate = "21/10/2023",
            bridgeMaterial = BridgeMaterial.Iron
        ),
        Bridge(
            id = 2,
            imagePath = Uri.parse("android.resource://${R::class.java.`package`?.name}/${R.drawable.asura}")
                .toString(),
            name = "Jembatan Suramadu",
            nationalNumber = 11,
            cityNumber = 122,
            tollNumber = 222,
            buildDate = "12/01/2021",
            latitudePosition = -7.2019638,
            longitudePosition = 112.7765428,
            mapLocName = "Bangkalan, Surabaya, Jawa Timur",
            provinceCity = "Surabaya, Jawa Timur",
            length = 221.3,
            wide = 11.4,
            inspectionPlanDate = "15/10/2023",
            bridgeMaterial = BridgeMaterial.Iron
        ),
        Bridge(
            id = 3,
            imagePath = Uri.parse("android.resource://${R::class.java.`package`?.name}/${R.drawable.jembatan_pik}")
                .toString(),
            name = "Jembatan PIK",
            nationalNumber = 11,
            cityNumber = 122,
            tollNumber = 222,
            buildDate = "12/01/2021",
            latitudePosition = -6.0853505,
            longitudePosition = 106.7238213,
            mapLocName = "Dadap, Kota Tangerang, Banten",
            provinceCity = "Tangerang, Banten",
            length = 221.3,
            wide = 11.4,
            inspectionPlanDate = "18/10/2023",
            bridgeMaterial = BridgeMaterial.Iron
        ),

        )
    val listBridgeCheck = mutableListOf<BridgeCheck>(
        BridgeCheck(bridgeId = 1),
        BridgeCheck(bridgeId = 1),
        BridgeCheck(bridgeId = 2),
        BridgeCheck(bridgeId = 2),
        BridgeCheck(bridgeId = 1),
        BridgeCheck(bridgeId = 1),
        BridgeCheck(bridgeId = 3)
    )


    var droneCamStatus = DroneCamConnectivityStatus.DISCONNECTED.string
        set(value) {
            field = value
            updateBridgePreviews()
        }


    private val bridgePreviews = generateBridgePreviews().toMutableList()

    fun getBridgePreviews(): List<BridgePreview> {
        updateBridgePreviews()
        return bridgePreviews
    }

    private fun updateBridgePreviews() {
        bridgePreviews.clear()
        bridgePreviews.addAll(generateBridgePreviews())
    }

    private fun generateBridgePreviews(): List<BridgePreview> {
        val bridgePreviews = mutableListOf(
            BridgePreview(
                -1,
                "",
                droneCamStatus,
                "",
                "",
                ""
            )
        )
        listBridges.forEach { bridgePreviews.add(it.toBridgePreview()) }
        return bridgePreviews
        /*val list = mutableListOf(
            BridgePreview(
                -1,
                -1,
                "",
                "")
        )
        (0..10).forEach { _ ->
            list.add(
                BridgePreview(
                    -1,
                    R.drawable.jembatan_siti_nurbaya,
                    "Jembatan Siti Nurbaya",
                    "Pemeriksaan terakhir: 20/08/2022"
                )
            )
        }
        return list*/
    }

    suspend fun getBridgePreviewsByQuery(query: String): List<BridgePreview> =
        withContext(Dispatchers.Default) {
            if (query.isBlank()) return@withContext getBridgePreviews()

            val bridgePreviews = mutableListOf(
                BridgePreview(
                    -1,
                    "",
                    droneCamStatus,
                    "",
                    "",
                    ""
                )
            )
            listBridges
                .filter { it.name.lowercase().contains(query.lowercase()) }
                .forEach { bridgePreviews.add(it.toBridgePreview()) }
            bridgePreviews
            /* bridgePreviews.apply {
                 removeAll { it.id != -1 }
                 addAll(
                     listBridges.filter { it.name.lowercase().contains(query.lowercase()) }
                     .map { it.toBridgePreview() }
                 )
             }*/
        }

    fun getBridgePreviewsByHistory(): List<BridgePreview> {

        val listBridgePreviewByHistory = mutableListOf(
            BridgePreview(-1, "", "", "", "", "")
        )

        if (listBridgeCheck.isEmpty()) return listBridgePreviewByHistory


        val sortedListBridgeCheck = listBridgeCheck.sortedByDescending { it.inspectionDate }

        val filteredBridgeCheck = sortedListBridgeCheck.distinctBy { it.bridgeId }



        filteredBridgeCheck.forEach { check ->
            val bridgeWithAssociatedId = listBridges.find { it.id == check.bridgeId }
            bridgeWithAssociatedId?.let {
                listBridgePreviewByHistory.add(it.toBridgePreview())
            }
        }
        return listBridgePreviewByHistory
    }

    fun getBridgeCheckPreview(selectedBridge: Bridge): List<BridgeCheckPreview> {
        val list = mutableListOf<BridgeCheckPreview>()

        val bridgeChecks = listBridgeCheck.filter { it.bridgeId == selectedBridge.id }

        bridgeChecks.forEach {
            val date = it.inspectionDate.toString(DATE_FORMAT_PATTERN)
            list.add(
                BridgeCheckPreview(
                    it.id,
                    selectedBridge.imagePath,
                    selectedBridge.name,
                    "Pemeriksaan terakhir: $date"
                )
            )
        }

        return list
        /*(0..10).forEach { i ->
            list.add(
                BridgeCheckPreview(
                    R.drawable.jembatan_siti_nurbaya,
                    "Jembatan Siti Nurbaya",
                    "Pemeriksaan terakhir: 20/0${1+i}/2022"
                )
            )
        }
        return list*/
    }

}

