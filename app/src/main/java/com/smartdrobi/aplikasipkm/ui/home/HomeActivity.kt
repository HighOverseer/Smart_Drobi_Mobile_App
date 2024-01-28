package com.smartdrobi.aplikasipkm.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.longdo.mjpegviewer.MjpegView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ActivityHomeBinding
import com.smartdrobi.aplikasipkm.domain.helper.DRONE_CAM_IP_ADDRESS
import com.smartdrobi.aplikasipkm.domain.helper.showToast
import com.smartdrobi.aplikasipkm.ui.home.otherview.DetailFragment
import com.smartdrobi.aplikasipkm.ui.home.otherview.NonTopLevelFragmentCallback
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.FragmentActivityCallback
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.GuideFragment
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.HistoryFragment
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.HomeFragment

class HomeActivity
    : AppCompatActivity(),
    FragmentActivityCallback,
    AddBridgeLauncher,
    AddBridgeCheckLauncher,
    EditBridgeCheckLauncher {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewDroneCam()
        setUpNavigation()

        binding.apply {
            bottomNavigationView.setOnItemReselectedListener {
                val currFragment = getCurrentFragment()
                if (
                    currFragment !is HomeFragment &&
                    currFragment !is GuideFragment &&
                    currFragment !is HistoryFragment
                ) {
                    when (it.itemId) {
                        R.id.home -> {
                            (currFragment as NonTopLevelFragmentCallback)
                                .popUpBackStack()
                        }
                    }
                }
            }

            binding.containerDroneCam.setOnClickListener {
                when (navController.currentDestination?.id) {
                    R.id.home -> navController.navigate(R.id.action_home_to_droneCamRecordActivity2)
                    R.id.detailFragment -> navController.navigate(R.id.action_detailFragment_to_droneCamRecordActivity2)
                }
            }
        }
    }

    private fun getCurrentFragment(): Fragment {
        return navHostFragment.childFragmentManager.fragments[0]
    }

    private fun setUpNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_framgent) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.guide,
                R.id.home,
                R.id.history
            )
        )
    }

    fun initViewDroneCam(startEagerly: Boolean = false) {
        binding.apply {
            if (startEagerly) viewDroneCam.stopStream()

            viewDroneCam.apply {
                mode = MjpegView.MODE_STRETCH

                isAdjustHeight = true
                setUrl(DRONE_CAM_IP_ADDRESS)
            }

            if (startEagerly) viewDroneCam.startStream()
        }
    }


    override fun keepBottomNavSelected(menuId: Int) {
        binding.bottomNavigationView.apply {
            when (menuId) {
                R.id.guide, R.id.home, R.id.history -> {
                    val menuItem = menu.findItem(menuId)
                    menuItem.isChecked = true
                }
            }
        }
    }

    private val onDestinationChangeListener by lazy {
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.home && destination.id != R.id.detailFragment) {
                binding.viewDroneCam.stopStream()
                binding.viewDroneCam.isVisible = false
            } else {
                binding.viewDroneCam.startStream()
                binding.viewDroneCam.isVisible = true
            }
        }
    }

    private val addBridgeFormLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != ADD_BRIDGE_RESULT_SUCCESS) return@registerForActivityResult

    }

    private val addBridgeCheckFormLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != ADD_CHECK_RESULT_SUCCESS) return@registerForActivityResult

        showToast(
            this@HomeActivity,
            getString(R.string.berhasil_menambahkan_form_pemeriksaan_jembatan)
        )

        val currFragment = getCurrentFragment()

        if (currFragment !is DetailFragment) return@registerForActivityResult

        navController.navigate(
            R.id.action_detailFragment_to_home
        )
        binding.apply {
            bottomNavigationView.selectedItemId = R.id.history
        }
    }

    private val editBridgeCheckFormLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != EDIT_CHECK_RESULT_SUCCESS) return@registerForActivityResult

        showToast(
            this@HomeActivity,
            getString(R.string.berhasil_mengupdate_form_pemeriksaan_jembatan)
        )

    }

    override fun launchAddBridgeCheckSession(intent: Intent) {
        addBridgeCheckFormLauncher.launch(intent)
    }

    override fun launchEditBridgeCheckSession(intent: Intent) {
        editBridgeCheckFormLauncher.launch(intent)
    }

    override fun launchAddBridgeAct(intent: Intent) {
        addBridgeFormLauncher.launch(intent)
    }

    override fun onResume() {
        navController.addOnDestinationChangedListener(onDestinationChangeListener)
        binding.viewDroneCam.startStream()
        super.onResume()
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(onDestinationChangeListener)
        binding.viewDroneCam.stopStream()
        super.onPause()
    }

    override fun onStop() {
        binding.viewDroneCam.stopStream()
        super.onStop()
    }

    companion object {
        const val ADD_BRIDGE_RESULT_SUCCESS = 10
        const val ADD_CHECK_RESULT_SUCCESS = 100
        const val EDIT_CHECK_RESULT_SUCCESS = 200
    }

}



