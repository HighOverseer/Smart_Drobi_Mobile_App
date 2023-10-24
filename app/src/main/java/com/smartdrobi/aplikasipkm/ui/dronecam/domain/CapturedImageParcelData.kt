package com.smartdrobi.aplikasipkm.ui.dronecam.domain

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

sealed class CapturedImageParcelData private constructor(){

    @Parcelize
    data class IncludeParent(
        val parentFieldPosition:Int,
        val fieldPosition:Int,
        val imageFile:File
    ):CapturedImageParcelData(),Parcelable

    @Parcelize
    data class NonIncludeParent(
        val fieldPosition:Int,
        val imageFile: File
    ):CapturedImageParcelData(), Parcelable
}