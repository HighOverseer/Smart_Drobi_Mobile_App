package com.smartdrobi.aplikasipkm.ui.dronecam.domain

import android.graphics.Bitmap

interface OnCaptureSessionSuccess {
    fun saveImage(imageBitmap: Bitmap)
}