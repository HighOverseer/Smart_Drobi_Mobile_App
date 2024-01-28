package com.smartdrobi.aplikasipkm.ui.adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemHeaderLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemHeaderWithoutSearchBarLayoutBinding
import com.smartdrobi.aplikasipkm.databinding.HomeCheckScheduleItemLayoutBinding
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview
import com.smartdrobi.aplikasipkm.domain.model.SearchState

//used in history fragment
class BridgePreviewsAdapter(
    private val items: List<BridgePreview>,
    private val onItemClickedEvent: OnItemClickedEvent,
    private val isHeaderWithSearchBar: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        HEADER, ITEM_LAYOUT
    }

    sealed class ItemCallbackAction private constructor() {
        data object ClearFocus : ItemCallbackAction()

        data class Search(val searchState: SearchState) : ItemCallbackAction()

        data class ClickItem(val itemPosition: Int) : ItemCallbackAction()

    }

    /*    private var initSearchBarQuery:String?=null
        private var initSearchBarFocus:Boolean = false

        constructor(
            items:List<BridgePreview>,
            onItemClickedEvent: OnItemClickedEvent,
            isHeaderWithSearchBar: Boolean,
            initSearchBarFocus:Boolean,
            initSearchBarQuery:String?
        ):this(items, onItemClickedEvent, isHeaderWithSearchBar){
            this.initSearchBarFocus = initSearchBarFocus
            this.initSearchBarQuery = initSearchBarQuery
        }*/


    class ItemViewHolder(
        val binding: HomeCheckScheduleItemLayoutBinding,
        sendAction: (ItemCallbackAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

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
                tvLastInspectionDate.setOnClickListener(clickListener)
                tvNextInspectionDate.setOnClickListener(clickListener)
                tvLocation.setOnClickListener(clickListener)

            }
        }

    }

    class HeaderWithoutSearchBarVH(
        val binding: HomeCheckScheduleItemHeaderWithoutSearchBarLayoutBinding,
        sendAction: (ItemCallbackAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
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
        sendAction: (ItemCallbackAction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        init {

            binding.apply {
                root.setOnClickListener {
                    sendAction(ItemCallbackAction.ClearFocus)
                }

                /*initSearchBarQuery?.let {searchBar.setQuery(it, false)}

                if (initSearchBarFocus){
                    searchBar.requestFocus()
                }else {
                    searchBar.clearEditTextFocus()
                }*/
                /*     searchBar.editText.addTextChangedListener(
                         afterTextChanged = { text: Editable? ->
                             text?.let {
                                 sendAction(
                                     ItemCallbackAction.Search(
                                         SearchState(
                                             it.toString(),
                                             true
                                         )
                                     )
                                 )
                             }

                         }
                     )*/

                searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                        val q = newText ?: ""
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
                if (isHeaderWithSearchBar) {
                    val binding = HomeCheckScheduleItemHeaderLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    /*return HeaderViewHolder(binding, initSearchBarFocus, initSearchBarQuery){
                        onItemClickedEvent.searchBridge(it)
                    }*/
                    return HeaderViewHolder(binding, ::onAction)
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
        when (getItemViewType(holder.adapterPosition)) {
            ViewType.HEADER.ordinal -> {
                return

                /*(holder as FragmentViewHolder).apply {
                    //onItemClickedEvent.onOpenFragmentCallback()
                }*/
            }

            else -> {
                val currItem = items[position]
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

        }

    }


    private fun onAction(action: ItemCallbackAction) {
        when (action) {
            ItemCallbackAction.ClearFocus -> onItemClickedEvent.clearFocus()
            is ItemCallbackAction.Search -> onItemClickedEvent.searchBridge(action.searchState)
            is ItemCallbackAction.ClickItem -> onItemClickedEvent.onItemClicked(items[action.itemPosition].id)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ViewType.HEADER.ordinal
        } else ViewType.ITEM_LAYOUT.ordinal
    }

    interface OnItemClickedEvent {
        //fun finishSearching()

        fun searchBridge(searchState: SearchState)

        fun onItemClicked(itemClickedId: Int)

        fun clearFocus()

    }
}