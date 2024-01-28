package com.smartdrobi.aplikasipkm.ui.home.toplevelview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartdrobi.aplikasipkm.databinding.FragmentHistoryBinding
import com.smartdrobi.aplikasipkm.domain.helper.obtainViewModel
import com.smartdrobi.aplikasipkm.domain.model.BridgePreview
import com.smartdrobi.aplikasipkm.domain.model.SearchState
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsItemDecoration
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.home.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var binding: FragmentHistoryBinding? = null
    private lateinit var viewModel: HistoryViewModel

    private lateinit var adapter: BridgePreviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        //initSp()

        /*   binding?.apply {
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
           }*/

        binding?.apply {
            viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                progressBar.isVisible = uiState.isLoading

                uiState.bridgeCheckIdForSelectedBridgeEvent
                    ?.getContentIfNotHandled()?.let { id ->
                        goToBridgeCheckForm(id)
                        return@observe
                    }
                setAdapter(uiState.listBridgePreviewsByHistory)

                //if only header
                tvEmptyInfo.isVisible = uiState.listBridgePreviewsByHistory.size == 1
            }
        }
    }

    private fun setAdapter(listBridgePreviewsByHistory: List<BridgePreview>) {
        adapter = BridgePreviewsAdapter(
            listBridgePreviewsByHistory,
            onItemAdapterClickedEvent,
            false
        )
        binding?.recyclerView?.adapter = adapter
    }

    private fun goToBridgeCheckForm(selectedBridgeCheckId: Int) {
        val intent = Intent(activity, AddBridgeCheckFormActivity::class.java)
        intent.putExtra(AddBridgeCheckFormActivity.ADD_MODE_KEY, false)
        intent.putExtra(AddBridgeCheckFormActivity.MODE_ID_KEY, selectedBridgeCheckId)
        requireActivity().startActivity(intent)
    }

    private fun init() {
        viewModel = obtainViewModel<
                HistoryViewModel.ViewModelFactory,
                HistoryViewModel>(this, requireActivity())

        binding?.apply {
            recyclerView.apply {
                addItemDecoration(
                    BridgePreviewsItemDecoration(
                        resources.displayMetrics,
                        paddingBottom = 15,
                        includeLastOne = true
                    )
                )
                layoutManager = LinearLayoutManager(requireActivity())
            }

        }
    }

    private val onItemAdapterClickedEvent by lazy {
        object : BridgePreviewsAdapter.OnItemClickedEvent {
            override fun clearFocus() {}
            override fun searchBridge(searchState: SearchState) {}
            override fun onItemClicked(itemClickedId: Int) {
                /*val sortedBridgeCheck = Dummy.listBridgeCheck.sortedByDescending { it.inspectionDate }
                val latestBridgeCheck = sortedBridgeCheck.find { it.bridgeId == itemClickedId } ?: return

                val intent = Intent(activity, AddBridgeCheckFormActivity::class.java)
                intent.putExtra(AddBridgeCheckFormActivity.ADD_MODE_KEY, false)
                intent.putExtra(AddBridgeCheckFormActivity.MODE_ID_KEY, latestBridgeCheck.id)
                requireActivity().startActivity(intent)*/

                viewModel.getLatestBridgeCheckIdInEachBridge(itemClickedId)
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