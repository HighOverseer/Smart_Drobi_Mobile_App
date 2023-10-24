package com.smartdrobi.aplikasipkm.ui.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ImageItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.helper.getDrawable
import com.smartdrobi.aplikasipkm.domain.helper.loadImage

class ImageCollectionAdapter(
    images:List<String?>,
    spanCount:Int,
    columnCount:Int,
    private val fieldPosition: Int,
    private val parentFieldPositionIfAny: Int = -1,
    private val callback:OnImageCollectionCallback
):RecyclerView.Adapter<ImageCollectionAdapter.ImageCollectionViewHolder>() {

    private val listImages:List<String?>
    private var plusSignPos:Int = -1

    init {
        listImages = images.toMutableList().let {
            it.add(null)

            val fixImages = MutableList<String?>(columnCount*spanCount){
                null
            }

            for (pos in it.indices){

                val startPos = (pos%columnCount)*spanCount
                val row = pos.div(columnCount)
                val finishPos = startPos+row
                fixImages[finishPos] = it[pos]

                if (pos == it.lastIndex){
                    plusSignPos = finishPos
                }
            }
            fixImages
        }
    }
    class ImageCollectionViewHolder(
        val binding:ImageItemLayoutBinding,
    ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCollectionViewHolder {
        val binding = ImageItemLayoutBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return ImageCollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageCollectionViewHolder, position: Int) {
        val resources = holder.itemView.context.resources
        try {
            val currImage = listImages[position]
            val context = holder.itemView.context
            if (currImage != null){
                holder.binding.ivImage.loadImage(
                    context,
                    currImage
                )
            }else if(position == plusSignPos){
                val addSignDrawable = getDrawable(
                    resources, R.drawable.btn_add_image_bg
                )
                holder.binding.ivImage.apply {
                    setBackgroundDrawable(
                        addSignDrawable
                    )
                    setOnClickListener {
                        showImageChooser()
                    }
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun showImageChooser(){
        val isParentAChild = parentFieldPositionIfAny != -1
        if (isParentAChild){
            callback.showDialogImageChooserInChild(
                parentFieldPositionIfAny,
                fieldPosition
            )
        }else callback.showDialogImageChooser(fieldPosition)
    }


    override fun getItemCount() = listImages.size

    interface OnImageCollectionCallback{
        fun showDialogImageChooserInChild(
            parentFieldPosition:Int,
            fieldPosition:Int
        )
        fun showDialogImageChooser(
            fieldPosition:Int
        )
    }
}