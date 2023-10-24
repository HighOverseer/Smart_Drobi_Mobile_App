package com.smartdrobi.aplikasipkm.ui.home.toplevelview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentHomeBinding
import com.smartdrobi.aplikasipkm.domain.helper.Dummy
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsAdapter
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsItemDecoration
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeFormActivity
import com.smartdrobi.aplikasipkm.ui.customeview.MySearchView
import com.smartdrobi.aplikasipkm.ui.home.AddBridgeLauncher
import com.smartdrobi.aplikasipkm.ui.home.OnAddBridgeSuccessListener
import com.smartdrobi.aplikasipkm.ui.home.otherview.DetailFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnAddBridgeSuccessListener {

    private var binding:FragmentHomeBinding?=null
    private lateinit var adapter: BridgePreviewsAdapter

    private var searchJob:Job? = null

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

        binding?.apply {
            root.setOnClickListener {
                clearAllViewFocus()
            }

            val data = Dummy.getBridgePreviews()
            adapter = BridgePreviewsAdapter(
                data,
                onItemAdapterClickedEvent,
                true
            )
            recyclerView.apply {
                addItemDecoration(
                    BridgePreviewsItemDecoration(
                        resources.displayMetrics,
                        paddingBottom = 15
                    )
                )
                this.adapter = this@HomeFragment.adapter
                layoutManager = LinearLayoutManager(requireActivity())
            }

            tvEmptyInfo.isVisible = data.size == 1

            //getRefToSearchBar()?.editText?.requestFocus()
            /*getRefToSearchBar()?.apply {
                lastQuery?.let { editText.setText(it) }
                if (searchBarEditTextFocusState){
                    editText.requestFocus()
                }else editText.clearFocus()
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

    private fun init(savedInstanceState: Bundle?){
        savedInstanceState?.let {
            lastQuery = it.getString(QUERY_KEY)
            searchBarEditTextFocusState = it.getBoolean(FOCUS_KEY)
        }
    }

    private val onItemAdapterClickedEvent:BridgePreviewsAdapter.OnItemClickedEvent by lazy {
        object:BridgePreviewsAdapter.OnItemClickedEvent{
            /*   override fun finishSearching() {
                binding?.apply {
                    recyclerView.scrollToPosition(0)
                    val headerView = recyclerView
                        .layoutManager
                        ?.findViewByPosition(0)
                    val searchBar = headerView?.findViewById<MySearchView>(R.id.search_bar)


                    searchBar?.clearEditTextFocus()
                    clearAllViewFocus()
                }
            }*/
            override fun clearFocus() {
                searchBarEditTextFocusState = false
                clearAllViewFocus()
            }

            override fun searchBridge(searchState: SearchState) {
                searchJob?.cancel()

                searchJob = lifecycleScope.launch {
                    var delayTime = 500L
                    if (!searchState.hasFocus) delayTime = 0L

                    delay(delayTime)

                    this@HomeFragment.lastQuery = searchState.query
                    this@HomeFragment.searchBarEditTextFocusState = searchState.hasFocus
                    val data = Dummy.getBridgePreviewsByQuery(searchState.query)
                    adapter = BridgePreviewsAdapter(
                        data,
                        onItemAdapterClickedEvent,
                        true
                    )
                    binding?.apply {
                        //recyclerView.swapAdapter(adapter, false)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        tvEmptyInfo.isVisible = data.size == 1
                        if (searchBarEditTextFocusState){
//                            root.clearFocus()
//                            recyclerView.focusedChild.clearFocus()
                            getRefToSearchBar()?.editText?.requestFocus()
                        }
                    }


                    /*binding?.apply {
                        recyclgetRefToSearchBar()?.editText = {SearchView$SearchAutoComplete@18571} "androidx.appcompat.widget.SearchView$SearchAutoComplete{9cd2315 VFED..CL. .F...AID 0,-8-683,91 #7f0a01df app:id/search_src_text aid=1073741824}"erView.swapAdapter(adapter, false)

                        tvEmptyInfo.isVisible = data.size == 1

                        recyclerView.scrollToPosition(0)
                        val headerView = recyclerView
                            .layoutManager
                            ?.findViewByPosition(0)
                        val searchBar = headerView?.findViewById<MySearchView>(R.id.search_bar)

                        searchBarEditTextFocusState = searchState.hasFocus

                        searchBar?.setQuery(lastQuery, false)

                        if (searchState.hasFocus){
                            searchBar?.requestFocus()
                        }else {
                            clearAllViewFocus()
                        }
                    }*/
                }
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

    override fun updateAdapter() {
        val data = Dummy.getBridgePreviews()
        adapter = BridgePreviewsAdapter(
            data,
            onItemAdapterClickedEvent,
            true
        )
        binding?.apply {
            recyclerView.swapAdapter(adapter, false)

            tvEmptyInfo.isVisible = data.size == 1
        }
    }

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