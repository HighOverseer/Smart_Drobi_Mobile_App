package com.smartdrobi.aplikasipkm.ui.dronecam

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.longdo.mjpegviewer.MjpegView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.FragmentDroneCamBinding
import com.smartdrobi.aplikasipkm.domain.helper.DRONE_CAM_IP_ADDRESS
import com.smartdrobi.aplikasipkm.domain.model.CaptureImageBitmapParcel


class DroneCamFragment : Fragment() {

    private var binding: FragmentDroneCamBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDroneCamBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDroneCam()

        binding?.apply {
            ibBack.setOnClickListener {
                requireActivity().finish()
            }
            btnCapture.setOnClickListener {
                val args = parcelBitmap(
                    viewDroneCam.drawToBitmap()
                )
                args?.also {
                    view.findNavController().navigate(
                        R.id.action_droneCamFragment_to_capturedImageFragment2,
                        args
                    )
                } ?: requireActivity().finish()
            }
        }
    }

    private fun parcelBitmap(bitmap: Bitmap): Bundle? {
        binding?.apply {
            return Bundle().also {
                val data = CaptureImageBitmapParcel(
                    bitmap
                )
                it.putParcelable(
                    CapturedImageFragment.BITMAP_KEY,
                    data
                )
            }
        }
        return null

    }

    override fun onResume() {
        binding?.viewDroneCam?.startStream()
        super.onResume()
    }

    override fun onPause() {
        binding?.viewDroneCam?.stopStream()
        super.onPause()
    }

    override fun onStop() {
        binding?.viewDroneCam?.stopStream()
        super.onStop()
    }

    private fun initViewDroneCam() {
        binding?.apply {
            viewDroneCam.apply {
                mode = if (isInPortraitMode()) {
                    MjpegView.MODE_FIT_HEIGHT
                } else MjpegView.MODE_FIT_WIDTH

                isAdjustHeight = true
                setUrl(DRONE_CAM_IP_ADDRESS)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun isInPortraitMode(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }


}