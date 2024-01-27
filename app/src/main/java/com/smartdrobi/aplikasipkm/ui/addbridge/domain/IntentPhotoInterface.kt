package com.smartdrobi.aplikasipkm.ui.addbridge.domain

interface IntentPhotoInterface {
    fun openGallery(
        fieldPosition: Int,
        parentFieldPosition: Int = -1
    )

    fun openCamera(
        fieldPosition: Int,
        parentFieldPosition: Int = -1
    )

    fun openCameraDrone(
        fieldPosition: Int,
        parentFieldPosition: Int = -1
    )
}