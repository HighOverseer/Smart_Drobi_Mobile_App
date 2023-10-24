package com.smartdrobi.aplikasipkm.ui.home.otherview

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentDetailBinding
import com.smartdrobi.aplikasipkm.domain.helper.Dummy
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.domain.helper.showDialogConfirmation
import com.smartdrobi.aplikasipkm.domain.helper.showToast
import com.smartdrobi.aplikasipkm.domain.model.Bridge
import com.smartdrobi.aplikasipkm.ui.adapter.BridgePreviewsItemDecoration
import com.smartdrobi.aplikasipkm.ui.adapter.DetailBridgeCheckPreviewAdapter
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeCheckFormActivity
import com.smartdrobi.aplikasipkm.ui.home.AddBridgeCheckLauncher
import com.smartdrobi.aplikasipkm.ui.home.EditBridgeCheckLauncher
import com.smartdrobi.aplikasipkm.ui.home.toplevelview.FragmentActivityCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class DetailFragment : Fragment(), NonTopLevelFragmentCallback {

    private var binding:FragmentDetailBinding?=null

    private lateinit var selectedBridge:Bridge

    private var textWatcherJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding?.apply {

            btnStartCheck.setOnClickListener {
                val activity = requireActivity()

                if (activity !is AddBridgeCheckLauncher) return@setOnClickListener

                etNextInspectionDate.setText(selectedBridge.inspectionPlanDate)
                val intent = Intent(activity, AddBridgeCheckFormActivity::class.java)
                intent.putExtra(AddBridgeCheckFormActivity.ADD_MODE_KEY, true)
                intent.putExtra(AddBridgeCheckFormActivity.MODE_ID_KEY, selectedBridge.id)
                activity.launchAddBridgeCheckSession(intent)
            }

            etNextInspectionDate.setOnClickListener {
                showDatePicker(it)
            }
            etNextInspectionDate.addTextChangedListener(
                afterTextChanged = ::etNextInspectionWatcher
            )

            btnSave.setOnClickListener {
                updateNextInspectionDate()
            }

            ibMap.setOnClickListener {
                showDialogConfirmation(
                    requireActivity(),
                    getString(R.string.yakin_ingin_menampilkan_lokasi_di_map),
                    { goToMap() }
                )
            }
        }
    }

    private fun goToMap(){
        val lat = selectedBridge.latitudePosition
        val lon = selectedBridge.longitudePosition
        val locName = selectedBridge.mapLocName
        val mapUri = Uri.parse("geo:$lat,$lon?q=${Uri.encode(locName)}")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        if (intent.resolveActivity(requireActivity().packageManager) != null){
            startActivity(intent)
            return
        }

        showToast(
            requireActivity(),
            getString(
                R.string.maaf_tidak_ada_aplikasi_yang_mensupport_aksi_ini
            )
        )
    }

    private fun updateNextInspectionDate() {
        binding?.apply {
            Dummy.apply {
                (0..listBridges.lastIndex).forEach { i  ->
                    val currBridge = listBridges[i]
                    if (currBridge.id == selectedBridge.id){
                        listBridges[i] = currBridge.copy(
                            inspectionPlanDate = etNextInspectionDate.text.toString()
                        )
                        selectedBridge = listBridges[i]
                        showToast(
                            requireActivity(),
                            getString(R.string.berhasil_disimpan)
                        )
                        setDesc()
                    }
                }
            }
        }
    }

    private fun init(){
        arguments?.let { args ->
            val id = args.getInt(SELECTED_BRIDGE_ID_KEY, -1)

            val bridge = Dummy.listBridges.find { it.id == id }

            if (bridge != null) {
                selectedBridge = bridge
            }else popUpBackStack()

        }

        val activityFragment = requireActivity()
        if (activityFragment is FragmentActivityCallback){
            activityFragment.keepBottomNavSelected(R.id.home)
        }

        setDesc()
    }

    private fun setDesc(){
        binding?.apply {
            selectedBridge.apply {
                tvName.text = name

                ivImage.loadImage(
                    requireActivity(),
                    imagePath
                )

                val inspectionDate = lastInspectionDate ?: "-"

                tvLastInspectionDate.text = getString(
                    R.string.pemeriksaan_terakhir_1_s,
                    inspectionDate
                )

                tvNationalNumber.text = getString(
                    R.string._1_ruas_jalan_nasional_dan_provinsi_100,
                    nationalNumber.toString()
                )

                tvCityNumber.text = getString(
                    R.string._2_ruas_jalan_kabupaten_dan_kota_122,
                    cityNumber.toString()
                )

                tvTollNumber.text = getString(
                    R.string._3_ruas_jalan_tol_122,
                    tollNumber.toString()
                )

                tvBuildDate.text = getString(
                    R.string.tanggal_pembangunan_12_10_2020,
                    buildDate
                )

                tvAdress.text = getString(
                    R.string.alamat_1_s,
                    mapLocName
                )

                tvProvinceCity.text = getString(
                    R.string.provinsi_kabupaten_kota_1_s,
                    provinceCity
                )

                tvLength.text = getString(
                    R.string.panjang_jembatan_1_s,
                    length.toString()
                )

                tvWide.text = getString(
                    R.string.luas_jembatan_1_s,
                    wide.toString()
                )

                tvMaterial.text = getString(
                    R.string.bahan_jembatan_1_s,
                    bridgeMaterial.stringVal
                )

                etNextInspectionDate.setText(inspectionPlanDate)

                rvCheckHistory.apply {
                    layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    addItemDecoration(
                        BridgePreviewsItemDecoration(
                            resources.displayMetrics,
                            paddingTop = 2,
                            paddingStart = 2,
                            paddingBottom = 4,
                            paddingEnd = 4
                        )
                    )
                    val data = Dummy.getBridgeCheckPreview(selectedBridge)
                    adapter = DetailBridgeCheckPreviewAdapter(
                        data,
                        ::checkHistoryClickAction
                    )

                    tvEmptyInfo.isVisible = data.isEmpty()


                }
            }
        }
    }

    private fun showDatePicker(clickedView: View){
        val editText = clickedView as AppCompatEditText
        val currSelectedDate = editText.text.toString()

        val year:Int
        val month:Int
        val day:Int

        if (currSelectedDate.isBlank()){
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        }else{
            val splittedDate = currSelectedDate.split("/")
            day = splittedDate[0].toInt()
            month = splittedDate[1].toInt() - 1
            year = splittedDate[2].toInt()
        }

        DatePickerDialog(requireActivity(), { _, yearr,monthOfTheYear, dayOfTheMonth ->
            val newDate = getString(
                R.string.date,
                dayOfTheMonth.toString(),
                monthOfTheYear.plus(1).toString(),
                yearr.toString()
            )
            editText.setText(
                newDate
            )
        }, year, month, day).show()
    }

    private fun etNextInspectionWatcher(
        s: Editable?
    ){
        textWatcherJob?.cancel()
        textWatcherJob = lifecycleScope.launch {
            delay(250L)
            binding?.apply {
                s?.let {
                    val isTheDefault = it.toString() == selectedBridge.inspectionPlanDate
                    if (isTheDefault){
                        btnSave.isEnabled = false
                        btnSave.alpha = 0.6f
                        return@launch
                    }

                    btnSave.isEnabled = true
                    btnSave.alpha = 1f
                }
            }

        }

    }

    private fun checkHistoryClickAction(clickedItemId:Int){
        val activity = requireActivity()
        if (activity !is EditBridgeCheckLauncher) return

        val selectedBridgeCheckHistory = Dummy.listBridgeCheck.find { it.id == clickedItemId }?:return

        binding?.etNextInspectionDate?.setText(selectedBridge.inspectionPlanDate)
        val intent = Intent(activity, AddBridgeCheckFormActivity::class.java)
        intent.putExtra(AddBridgeCheckFormActivity.ADD_MODE_KEY, false)
        intent.putExtra(AddBridgeCheckFormActivity.MODE_ID_KEY, selectedBridgeCheckHistory.id)
        activity.launchEditBridgeCheckSession(intent)
    }

    override fun popUpBackStack() {
        view?.findNavController()?.navigate(R.id.action_detailFragment_to_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcherJob?.cancel()
        binding = null
    }

    companion object{
        const val SELECTED_BRIDGE_ID_KEY = "key"
    }

}