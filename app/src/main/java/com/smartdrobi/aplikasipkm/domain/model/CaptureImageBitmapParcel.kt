package com.smartdrobi.aplikasipkm.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CaptureImageBitmapParcel(
    val imageBitmap: Bitmap
):Parcelable