package com.smartdrobi.aplikasipkm.ui.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemHeaderLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemHeaderWithoutSearchBarLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.ui.home.DroneCamConnectivityStatus
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.SearchState

//used in home fragment, more suitable with search bar
class BridgePreviewsListAdapter(
    private val onItemClickedEvent: OnItemClickedEvent,
    private val isHeaderWithSearchBar:Boolean,
    private val initialDroneStatus:DroneCamConnectivityStatus
) : ListAdapter<BridgePreview, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    init {
        setHasStableIds(true)
    }
    enum class ViewType {
        HEADER, ITEM_LAYOUT
    }

    sealed class ItemCallbackAction private constructor(){
        data object ClearFocus:ItemCallbackAction()

        data class Search(val searchState: SearchState):ItemCallbackAction()

        data class ClickItem(val itemPosition:Int):ItemCallbackAction()

        data object ShowDroneCamSettingDialog:ItemCallbackAction()

    }


    class ItemViewHolder(
        val binding: HomeCheckScheduleItemLayoutBinding,
        sendAction:(ItemCallbackAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){

        private val clickListener = OnClickListener {
            sendAction(
                ItemCallbackAction.ClickItem(
                    adapterPosition
                )
            )
        }

        init {
            binding.root.setOnClickListener {
                sendAction(ItemCallbackAction.ClearFocus)
            }

            itemView.setOnClickListener(clickListener)

            binding.apply {

                tvName.setOnClickListener(clickListener)
                tvLastInspectionDate.setOnClickListener (clickListener)
                tvNextInspectionDate.setOnClickListener (clickListener)
                tvLocation.setOnClickListener(clickListener)

            }
        }

    }

    class HeaderWithoutSearchBarVH(
        val binding:HomeCheckScheduleItemHeaderWithoutSearchBarLayoutBinding,
        sendAction: (ItemCallbackAction) -> Unit
    ):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                sendAction(
                    ItemCallbackAction.ClearFocus
                )
            }
        }
    }

    class HeaderViewHolder(
        val binding: HomeCheckScheduleItemHeaderLayoutBinding,
        private val initialDroneStatus: DroneCamConnectivityStatus,
        sendAction:(ItemCallbackAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){


        fun setStatusDroneText(status:DroneCamConnectivityStatus){
            val statusColorResId = when(status){
                DroneCamConnectivityStatus.CONNECTED -> {
                    R.color.green
                }
                DroneCamConnectivityStatus.CONNECTING -> {
                    R.color.gold
                }
                DroneCamConnectivityStatus.DISCONNECTED -> {
                    R.color.red
                }
            }

            val statusColor = itemView.resources.getColor(statusColorResId, null)
            val spannableWord = SpannableString("Status Drone: ${status.string}")
            spannableWord.setSpan(ForegroundColorSpan(statusColor), 14, spannableWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tvInfoStatusDrone.text = spannableWord
        }

        init {
            setStatusDroneText(initialDroneStatus)

            binding.apply {
                root.setOnClickListener {
                    sendAction(ItemCallbackAction.ClearFocus)
                }

                binding.tvInfoStatusDrone.setOnClickListener {
                    sendAction(ItemCallbackAction.ShowDroneCamSettingDialog)
                }


                searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        sendAction(
                            ItemCallbackAction.ClearFocus
                        )
                        val q = query ?: ""
                        sendAction(
                            ItemCallbackAction.Search(
                                SearchState(
                                    q,
                                    false
                                )
                            )

                        )
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        val q = newText ?:""
                        sendAction(
                            ItemCallbackAction.Search(
                                SearchState(
                                    q,
                                    true
                                )
                            )
                        )
                        return true
                    }
                })
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.HEADER.ordinal -> {
                if (isHeaderWithSearchBar){
                    val binding = HomeCheckScheduleItemHeaderLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    /*return HeaderViewHolder(binding, initSearchBarFocus, initSearchBarQuery){
                        onItemClickedEvent.searchBridge(it)
                    }*/
                    return HeaderViewHolder(binding, initialDroneStatus, ::onAction)
                }

                val binding = HomeCheckScheduleItemHeaderWithoutSearchBarLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderWithoutSearchBarVH(binding, ::onAction)

            }

            else -> {
                val binding = HomeCheckScheduleItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemViewHolder(binding, ::onAction)
            }
        }
    }




    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val currItem = getItem(position)

        if (getItemViewType(holder.adapterPosition) == ViewType.HEADER.ordinal){
            /*(holder as HeaderViewHolder).apply {
                val status = DroneCamConnectivityStatus.values().first{ it.string == currItem.name }
                setStatusDroneText(status)
            }*/
            return
        }

        (holder as ItemViewHolder).apply {
            val context = itemView.context
            binding.apply {

                ivPreview.loadImage(context, currItem.imagePath)
                tvName.text = currItem.name
                tvLastInspectionDate.text = context.getString(
                    R.string.pemeriksaan_terakhir, currItem.lastInspectionDate
                )
                tvNextInspectionDate.text = context.getString(
                    R.string.pemeriksaan_selanjutnya, currItem.nextInspectionDate
                )
                tvLocation.text = context.getString(
                    R.string.lokasi_format, currItem.location
                )

            }

        }


    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    private fun onAction(action:ItemCallbackAction){
        when(action){
            ItemCallbackAction.ClearFocus -> onItemClickedEvent.clearFocus()
            is ItemCallbackAction.Search -> onItemClickedEvent.searchBridge(action.searchState)
            is ItemCallbackAction.ClickItem -> onItemClickedEvent.onItemClicked(getItem(action.itemPosition).id)
            is ItemCallbackAction.ShowDroneCamSettingDialog -> onItemClickedEvent.showDroneCamSettingDialog()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ViewType.HEADER.ordinal
        } else ViewType.ITEM_LAYOUT.ordinal
    }

    interface OnItemClickedEvent {

        fun searchBridge(searchState: SearchState)

        fun onItemClicked(itemClickedId:Int)

        fun clearFocus()

        fun showDroneCamSettingDialog()

    }


    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BridgePreview>(){
            override fun areItemsTheSame(oldItem: BridgePreview, newItem: BridgePreview): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BridgePreview,
                newItem: BridgePreview
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}