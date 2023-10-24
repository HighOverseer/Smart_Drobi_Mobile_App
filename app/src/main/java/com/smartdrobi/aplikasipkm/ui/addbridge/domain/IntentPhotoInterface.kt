package com.smartdrobi.aplikasipkm.ui.addbridge.domain

import java.text.FieldPosition

interface IntentPhotoInterface{
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