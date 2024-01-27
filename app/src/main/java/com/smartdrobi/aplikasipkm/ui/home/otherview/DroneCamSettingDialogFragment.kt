package com.smartdrobi.aplikasipkm.ui.home.otherview

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.smartdrobi.aplikasipkm.databinding.FragmentDialogDroneCamSettingBinding
import com.smartdrobi.aplikasipkm.ui.home.OnSettingDroneCamListener

class DroneCamSettingDialogFragment : DialogFragment() {

    private var binding: FragmentDialogDroneCamSettingBinding? = null

    private lateinit var listener: OnSettingDroneCamListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mFragment = parentFragment
        if (mFragment !is OnSettingDroneCamListener) return

        listener = mFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Material_Dialog_NoActionBar
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(android.graphics.Color.TRANSPARENT)
        )
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogDroneCamSettingBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnConnect.setOnClickListener {
                val newIP = etServerIp.text.toString()
                listener.onConnect(newIP)
                dialog?.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}