package com.smartdrobi.aplikasipkm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.DetailCheckHistoryItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckPreview

class DetailBridgeCheckPreviewAdapter(
    private val items: List<BridgeCheckPreview>,
    private val clickAction: (clickedItemId: Int) -> Unit
) : RecyclerView.Adapter<DetailBridgeCheckPreviewAdapter.DetailBridgeCheckPreviewViewHolder>() {

    class DetailBridgeCheckPreviewViewHolder(
        val binding: DetailCheckHistoryItemLayoutBinding,
        clickedItemPosition: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                clickedItemPosition(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailBridgeCheckPreviewViewHolder {
        val binding = DetailCheckHistoryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailBridgeCheckPreviewViewHolder(binding) { itemPosition ->
            clickAction(
                items[itemPosition].checkId
            )
        }
    }

    override fun onBindViewHolder(holder: DetailBridgeCheckPreviewViewHolder, position: Int) {
        val currItem = items[position]

        holder.apply {
            binding.apply {
                ivImage.loadImage(
                    itemView.context,
                    currItem.imagePreview
                )
                tvCheckDate.text = itemView.context.getString(
                    R.string.tanggal_pemeriksaan_tanggal,
                    currItem.lastInspectionDate
                )

                if (currItem.lastInspectionDate == "-") return

                tvMonth.text = getMonthsFromDate(itemView.context, currItem.lastInspectionDate)
            }
        }
    }

    private fun getMonthsFromDate(
        context: Context,
        stringDate: String
    ): String {
        var monthsPosition = 1
        try {
            monthsPosition = stringDate.split("/")[1].toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val months = context.resources.getStringArray(R.array.months_list)
        return months[monthsPosition]
    }

    override fun getItemCount() = items.size
}