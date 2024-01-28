package com.smartdrobi.aplikasipkm.ui.dronecam

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartdrobi.aplikasipkm.databinding.FragmentCapturedImageBinding
import com.smartdrobi.aplikasipkm.domain.helper.loadImage
import com.smartdrobi.aplikasipkm.domain.model.CaptureImageBitmapParcel
import com.smartdrobi.aplikasipkm.ui.dronecam.domain.OnCaptureSessionSuccess

class CapturedImageFragment : Fragment() {

    private var binding: FragmentCapturedImageBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCapturedImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = getDataParcel()
        if (data != null) {
            binding?.apply {
                ivCaptured.loadImage(
                    requireActivity(),
                    data.imageBitmap
                )

                btnFinish.setOnClickListener {
                    val fragmentActivity = requireActivity()
                    if (fragmentActivity is OnCaptureSessionSuccess) {
                        fragmentActivity.saveImage(
                            data.imageBitmap
                        )
                    }
                }
            }
        } else requireActivity().finish()

    }

    @Suppress("DEPRECATION")
    private fun getDataParcel(): CaptureImageBitmapParcel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(BITMAP_KEY, CaptureImageBitmapParcel::class.java)
        } else {
            arguments?.getParcelable(BITMAP_KEY)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    companion object {
        const val BITMAP_KEY = "bitmap"
    }
}