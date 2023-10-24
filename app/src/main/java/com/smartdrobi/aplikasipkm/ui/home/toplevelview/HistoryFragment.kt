package com.smartdrobi.aplikasipkm.ui.home.toplevelview

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.longdo.mjpegviewer.MjpegView

import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentHistoryBinding
import com.smartdrobi.aplikasipkm.domain.helper.Dummy
import com.smartdrobi.aplikasipkm.domain.helper.setInit
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsItemDecoration
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.home.otherview.DetailFragment

class HistoryFragment : Fragment() {

    private var binding:FragmentHistoryBinding?=null

    private lateinit var adapter:BridgePreviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initSp()

        binding?.apply {
            val data = Dummy.getBridgePreviewsByHistory()

            adapter = BridgePreviewsAdapter(
                data,
                onItemAdapterClickedEvent,
                false
            )
            recyclerView.apply {
                addItemDecoration(
                    BridgePreviewsItemDecoration(
                        resources.displayMetrics,
                        paddingBottom = 15
                    )
                )
                this.adapter = this@HistoryFragment.adapter
                layoutManager = LinearLayoutManager(requireActivity())
            }

            //if only header
            tvEmptyInfo.isVisible = data.size == 1
        }
    }

    private val onItemAdapterClickedEvent by lazy {
        object:BridgePreviewsAdapter.OnItemClickedEvent{
            override fun clearFocus() {}
            override fun searchBridge(searchState: SearchState) {}
            override fun onItemClicked(itemClickedId:Int) {
                val sortedBridgeCheck = Dummy.listBridgeCheck.sortedByDescending { it.inspectionDate }
                val latestBridgeCheck = sortedBridgeCheck.find { it.bridgeId == itemClickedId } ?: return

                val intent = Intent(activity, AddBridgeCheckFormActivity::class.java)
                intent.putExtra(AddBridgeCheckFormActivity.ADD_MODE_KEY, false)
                intent.putExtra(AddBridgeCheckFormActivity.MODE_ID_KEY, latestBridgeCheck.id)
                requireActivity().startActivity(intent)
            }
        }
    }

    /*private fun initSp(){
        binding?.apply {
            val years = resources.getStringArray(R.array.years_list).toList()
            spYear.setInit(requireActivity(), years)

            val months = resources.getStringArray(R.array.months_list).toList()
            spMonth.setInit(requireActivity(), months)
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}