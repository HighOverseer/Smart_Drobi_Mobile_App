package com.smartdrobi.aplikasipkm.ui.adapter

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.domain.helper.toDp

class ImageCollectionDecoration(
    private val displayMetrics: DisplayMetrics,
    private val padding:Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            right = padding.toDp(displayMetrics)
            bottom = padding.toDp(displayMetrics)
        }
    }


}