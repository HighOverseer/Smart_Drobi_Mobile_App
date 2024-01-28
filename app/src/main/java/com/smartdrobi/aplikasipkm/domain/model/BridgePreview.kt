package com.smartdrobi.aplikasipkm.domain.model

import androidx.viewbinding.ViewBinding
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.ui.home.DroneCamConnectivityStatus



data class BridgePreview(
    val id: Int,
    val imagePath: String,
    val name: String,
    val lastInspectionDate: String,
    val nextInspectionDate: String,
    val location: String
):ListItem{
    override fun getItemId(): Long {
        return id.toLong()
    }

    override fun equalsTo(any: Any): Boolean {
        return this == any
    }
}

data class SearchHeaderBridgePreview(
    val droneCamConnectivityStatus: DroneCamConnectivityStatus = DroneCamConnectivityStatus.DISCONNECTED,
    val searchState: SearchState = SearchState()
):ListItem{

    override fun getItemId(): Long {
        return -1
    }

    override fun equalsTo(any: Any): Boolean {
        return this == any
    }
}

