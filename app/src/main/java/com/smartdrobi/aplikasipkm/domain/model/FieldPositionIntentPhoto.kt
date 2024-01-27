package com.smartdrobi.aplikasipkm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FieldPositionIntentPhoto(
    val fieldPosition: Int,
    val parentFieldPosition: Int = -1
) : Parcelable