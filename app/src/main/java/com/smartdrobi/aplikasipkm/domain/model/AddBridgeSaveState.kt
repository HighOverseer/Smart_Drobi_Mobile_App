package com.smartdrobi.aplikasipkm.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.smartdrobi.aplikasipkm.domain.helper.JAKARTA_LAT
import com.smartdrobi.aplikasipkm.domain.helper.JAKARTA_LON
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddBridgeSaveState(
    val imagePath: String? = null,
    val position: LatLng = LatLng(JAKARTA_LAT, JAKARTA_LON),
    val mapLocName: String = "Jakarta"
) : Parcelable