package com.smartdrobi.aplikasipkm.ui.home.toplevelview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentHomeBinding
import com.smartdrobi.aplikasipkm.domain.helper.obtainViewModel
import com.smartdrobi.aplikasipkm.domain.model.SearchState
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsItemDecoration
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsListAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeFormActivity
import com.smartdrobi.aplikasipkm.ui.customeview.MySearchView
import com.smartdrobi.aplikasipkm.ui.home.AddBridgeLauncher
import com.smartdrobi.aplikasipkm.ui.home.OnSettingDroneCamListener
import com.smartdrobi.aplikasipkm.ui.home.otherview.DetailFragment
import com.smartdrobi.aplikasipkm.ui.home.otherview.DroneCamSettingDialogFragment
import com.smartdrobi.aplikasipkm.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment :
    Fragment(),
    OnSettingDroneCamListener {

    private var binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    //private lateinit var adapter: BridgePreviewsAdapter
    private lateinit var adapter: BridgePreviewsListAdapter

    private var searchJob: Job? = null

    private var lastQuery: String? = null
    private var searchBarEditTextFocusState: Boolean = false

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

            ibAdd.setOnClickListener {
                val activity = requireActivity()

                if (activity !is AddBridgeLauncher) return@setOnClickListener

                val intent = Intent(activity, AddBridgeFormActivity::class.java)
                activity.launchAddBridgeAct(intent)

            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bridgePreviewsWithSearchHeader.collectLatest {
                    adapter.submitList(it)

                    binding?.tvEmptyInfo?.isVisible = it.size == 1
                }
            }
        }
    }

    private fun showDroneCamSettingDialog() {
        val fragment = DroneCamSettingDialogFragment()
        fragment.show(childFragmentManager, null)
    }

    private fun init(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            lastQuery = it.getString(QUERY_KEY)
            searchBarEditTextFocusState = it.getBoolean(FOCUS_KEY)
        }

        viewModel = obtainViewModel<
                HomeViewModel.ViewModelFactory,
                HomeViewModel>(this, requireContext())

        adapter = BridgePreviewsListAdapter(
            onItemAdapterClickedEvent,
            true
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
                itemAnimator = null
                this.adapter = this@HomeFragment.adapter
            }

        }
    }

    private val onItemAdapterClickedEvent: BridgePreviewsListAdapter.OnItemClickedEvent by lazy {
        object : BridgePreviewsListAdapter.OnItemClickedEvent {
            override fun clearFocus() {
                searchBarEditTextFocusState = false
                clearAllViewFocus()
            }

            override fun searchBridge(searchState: SearchState) {
                this@HomeFragment.lastQuery = searchState.query
                this@HomeFragment.searchBarEditTextFocusState = searchState.hasFocus
                viewModel.searchBridgePreviews(searchState)

            }

            override fun onItemClicked(itemClickedId: Int) {
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
        val headerView = binding?.recyclerView
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


    override fun onConnect(newIP: String) {
        viewModel.connectDroneCam(newIP)
    }

    override fun onDestroyView() {
        searchJob?.cancel()
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val QUERY_KEY = "query"
        const val FOCUS_KEY = "focus"
    }


}