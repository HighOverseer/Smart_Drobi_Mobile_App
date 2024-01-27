package com.smartdrobi.aplikasipkm.ui.home.toplevelview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.query
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentHomeBinding
import com.smartdrobi.aplikasipkm.domain.helper.Dummy
import com.smartdrobi.aplikasipkm.domain.helper.obtainViewModel
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsItemDecoration
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsListAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeFormActivity
import com.smartdrobi.aplikasipkm.ui.customeview.MySearchView
import com.smartdrobi.aplikasipkm.ui.home.AddBridgeLauncher
import com.smartdrobi.aplikasipkm.ui.home.DroneCamConnectivityStatus
import com.smartdrobi.aplikasipkm.ui.home.OnAddBridgeSuccessListener
import com.smartdrobi.aplikasipkm.ui.home.OnSettingDroneCamListener
import com.smartdrobi.aplikasipkm.ui.home.otherview.DetailFragment
import com.smartdrobi.aplikasipkm.ui.home.otherview.DroneCamSettingDialogFragment
import com.smartdrobi.aplikasipkm.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment :
    Fragment(),
    OnAddBridgeSuccessListener,
    OnSettingDroneCamListener{

    private var binding:FragmentHomeBinding?=null
    private lateinit var viewModel: HomeViewModel
    //private lateinit var adapter: BridgePreviewsAdapter
    private lateinit var adapter:BridgePreviewsListAdapter

    private var searchJob:Job? = null

    private var connectDroneJob:Job?=null

    private var lastQuery:String?=null
    private var searchBarEditTextFocusState:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        setObservers()

        binding?.apply {
            root.setOnClickListener {
                clearAllViewFocus()
            }

            /*val data = Dummy.getBridgePreviews()
            adapter = BridgePreviewsAdapter(
                data,
                onItemAdapterClickedEvent,
                true
            )*/
     /*       adapter = BridgePreviewsListAdapter(
                onItemAdapterClickedEvent,
                true,
                DroneCamConnectivityStatus.entries.first{ it.string == Dummy.droneCamStatus }
            ).also {
                it.submitList(data)

                //sus
                it.setHasStableIds(true)
            }
            recyclerView.apply {
                addItemDecoration(
                    BridgePreviewsItemDecoration(
                        resources.displayMetrics,
                        paddingBottom = 15
                    )
                )
                this.adapter = this@HomeFragment.adapter
            }

            tvEmptyInfo.isVisible = data.size == 1*/


            /*viewModel.bridgePreviews.observe(viewLifecycleOwner){
                adapter.submitList(it)
                adapter.setHasStableIds(true)
                tvEmptyInfo.isVisible = it.size == 1
            }*/

            ibAdd.setOnClickListener {
                val activity = requireActivity()

                if (activity !is AddBridgeLauncher) return@setOnClickListener

                val intent = Intent(activity, AddBridgeFormActivity::class.java)
                activity.launchAddBridgeAct(intent)

                /*view.findNavController()
                    .navigate(
                        R.id.action_home_to_bridgeFormActivity,


                   )*/
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.bridgePreviews.collectLatest{
                    adapter.submitList(it)
                    //adapter.setHasStableIds(true)
                    binding?.tvEmptyInfo?.isVisible = it.size == 1
                }
            }
        }
    }

    private fun showDroneCamSettingDialog() {
        val fragment = DroneCamSettingDialogFragment()
        fragment.show(childFragmentManager, null)
    }

    private fun init(savedInstanceState: Bundle?){
        savedInstanceState?.let {
            lastQuery = it.getString(QUERY_KEY)
            searchBarEditTextFocusState = it.getBoolean(FOCUS_KEY)
        }

        viewModel = obtainViewModel<
                HomeViewModel.ViewModelFactory,
                HomeViewModel>(this, requireContext())

        adapter = BridgePreviewsListAdapter(
            onItemAdapterClickedEvent,
            true,
            DroneCamConnectivityStatus.entries.first{ it.string == Dummy.droneCamStatus }
        )
        binding?.apply {
            recyclerView.apply {
                binding?.recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
                addItemDecoration(
                    BridgePreviewsItemDecoration(
                        resources.displayMetrics,
                        paddingBottom = 15,
                        includeLastOne = true
                    )
                )
                this.adapter = this@HomeFragment.adapter
            }

        }
    }

    private val onItemAdapterClickedEvent:BridgePreviewsListAdapter.OnItemClickedEvent by lazy {
        object:BridgePreviewsListAdapter.OnItemClickedEvent{
            override fun clearFocus() {
                searchBarEditTextFocusState = false
                clearAllViewFocus()
            }

            override fun searchBridge(searchState: SearchState) {
                if (searchState.query == getRefToSearchBar()?.query) return

                this@HomeFragment.lastQuery = searchState.query
                this@HomeFragment.searchBarEditTextFocusState = searchState.hasFocus
                viewModel.searchBridgePreviews(searchState.query, searchState)

                /*searchJob?.cancel()

                searchJob = lifecycleScope.launch {
                    var delayTime = 500L
                    if (!searchState.hasFocus) delayTime = 0L

                    delay(delayTime)

                    this@HomeFragment.lastQuery = searchState.query
                    this@HomeFragment.searchBarEditTextFocusState = searchState.hasFocus
                    val data = Dummy.getBridgePreviewsByQuery(searchState.query)
                    val isEmptyInfoVisible = data.size == 1
                    if (!isEmptyInfoVisible){
                        adapter.notifyItemRangeInserted(1, data.size - 2)
                    }
                    binding?.tvEmptyInfo?.isVisible = isEmptyInfoVisible



                    *//*val data = Dummy.getBridgePreviewsByQuery(searchState.query)
                    adapter.submitList(data)
                    binding?.tvEmptyInfo?.isVisible = data.size == 1*//*

              *//*      binding?.apply {
                        tvEmptyInfo.isVisible = data.size == 1
                        lastQuery?.let {
                            getRefToSearchBar()?.editText?.setText(it)
                        }
                        if (searchBarEditTextFocusState){
                            getRefToSearchBar()?.editText?.requestFocus()
                        }
                    }*//*

                }*/
            }

            override fun onItemClicked(itemClickedId:Int) {
                val args = Bundle().also {
                    it.putInt(DetailFragment.SELECTED_BRIDGE_ID_KEY, itemClickedId)
                }
                view?.findNavController()?.navigate(
                    R.id.action_home_to_detailFragment,
                    args
                )
            }

            override fun showDroneCamSettingDialog() {
                this@HomeFragment.showDroneCamSettingDialog()
            }
        }
    }

    private fun clearAllViewFocus() {
        getRefToSearchBar()?.clearEditTextFocus()
        binding?.recyclerView?.focusedChild?.clearFocus()
        closeKeyboard()
    }

    private fun getRefToSearchBar(): MySearchView? {
        //binding?.recyclerView?.scrollToPosition(0)
        val headerView = binding?.recyclerView
            /*?.layoutManager
            ?.findViewByPosition(0)*/
            ?.getChildAt(0)
        return headerView?.findViewById(R.id.search_bar)
    }

    private fun closeKeyboard() {
        val imm = requireActivity()
            .getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

        imm.hideSoftInputFromWindow(
            view?.windowToken,
            0
        )
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lastQuery?.let {
            outState.putString(QUERY_KEY, it)
            outState.putBoolean(FOCUS_KEY, searchBarEditTextFocusState)
        }
    }

    //not used
    override fun updateAdapter() {
        val data = Dummy.getBridgePreviews()
        /*adapter = BridgePreviewsAdapter(
            data,
            onItemAdapterClickedEvent,
            true
        )*/
        /*adapter = BridgePreviewsListAdapter(
            onItemAdapterClickedEvent,
            true,
            DroneCamConnectivityStatus.values().first{ it.string == Dummy.droneCamStatus }
        )*/
        adapter.submitList(data)
        binding?.apply {
            recyclerView.swapAdapter(adapter, false)
            tvEmptyInfo.isVisible = data.size == 1
        }
    }

    override fun onConnect(newIP: String) {
        connectDroneJob?.cancel()
        connectDroneJob = lifecycleScope.launch {
            val vh = binding?.recyclerView?.findViewHolderForItemId(-1)
            if (vh !is BridgePreviewsListAdapter.HeaderViewHolder) return@launch

            Dummy.droneCamStatus = DroneCamConnectivityStatus.CONNECTING.string
            vh.setStatusDroneText(DroneCamConnectivityStatus.CONNECTING)

            //adapter.notifyItemChanged(0)

            delay(1500L)

            Dummy.droneCamStatus = DroneCamConnectivityStatus.CONNECTED.string
            vh.setStatusDroneText(DroneCamConnectivityStatus.CONNECTED)

            //adapter.notifyItemChanged(0)

        }
    }

   /* private fun getRefDroneStatus():AppCompatTextView?{
        binding?.apply {
            val headerView = binding?.recyclerView
                *//*?.layoutManager
                ?.findViewByPosition(0)*//*
                ?.getChildAt(0)
            return headerView?.findViewById(R.id.tv_info_status_drone)
        }
    }*/

    override fun onDestroyView() {
        searchJob?.cancel()
        super.onDestroyView()
        binding = null
    }

    companion object{
        const val QUERY_KEY = "query"
        const val FOCUS_KEY = "focus"
    }



}