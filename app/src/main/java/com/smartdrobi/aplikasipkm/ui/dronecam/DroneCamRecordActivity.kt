package com.smartdrobi.aplikasipkm.ui.dronecam

import android.content.res.Configuration
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import com.longdo.mjpegviewer.MjpegView
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ActivityDroneCamRecordBinding
import com.smartdrobi.aplikasipkm.domain.helper.DRONE_CAM_IP_ADDRESS
import com.smartdrobi.aplikasipkm.domain.helper.ViewRecorder
import java.io.IOException

class DroneCamRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDroneCamRecordBinding

    private var isRecording = false

    private lateinit var viewRecorder: ViewRecorder

    private lateinit var workerHandler: Handler
    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDroneCamRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewDroneCam()

        handlerThread = HandlerThread("bg_view_recorder")
        handlerThread.start()
        workerHandler = Handler(handlerThread.looper)


        binding.apply {
            ibBack.setOnClickListener {
                finish()
            }

            btnRecord.setOnClickListener {
                workerHandler.post {
                    if (!isRecording) {
                        runOnUiThread {
                            btnRecord.text = getString(R.string.stop)
                        }
                        startRecord()
                    } else {
                        runOnUiThread {
                            btnRecord.text = getString(R.string.rekam)
                        }

                        stopRecord()
                    }
                }
            }


        }

    }

    private fun startRecord() {
        val dir = applicationContext.externalCacheDir
        if (dir != null) {
            dir.mkdirs()
            if (!dir.exists()) {
                println("failed to start")
                return
            }
        }

        viewRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ViewRecorder(this)
        } else ViewRecorder()

        viewRecorder.apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoFrameRate(30)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoSize(
                binding.viewDroneCam.width,
                binding.viewDroneCam.height
            )
            setVideoEncodingBitRate(2000 * 1000)
            setOutputFile(
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/Aplikasi_PKM-${System.currentTimeMillis()}.mp4"
            )
            setOnErrorListener(onRecordErrorListener)

            setRecordedView(binding.viewDroneCam)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                return
            }
            isRecording = true
        }
    }

    private fun stopRecord() {
        try {
            viewRecorder.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isRecording = false
        println("Stop Success")
    }

    private val onRecordErrorListener by lazy {
        MediaRecorder.OnErrorListener { _, _, _ ->
            viewRecorder.reset()
            viewRecorder.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        workerHandler.looper.quit()
        handlerThread.quitSafely()
    }

    private fun initViewDroneCam() {
        binding.apply {
            viewDroneCam.apply {
                mode = if (isInPortraitMode()) {
                    MjpegView.MODE_FIT_HEIGHT
                } else MjpegView.MODE_FIT_WIDTH

                isAdjustHeight = true
                setUrl(DRONE_CAM_IP_ADDRESS)
            }
        }
    }

    private fun isInPortraitMode(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun onResume() {
        binding.viewDroneCam.startStream()
        super.onResume()
    }

    override fun onPause() {
        binding.viewDroneCam.stopStream()
        super.onPause()
    }

    override fun onStop() {
        binding.viewDroneCam.stopStream()
        super.onStop()
    }

}