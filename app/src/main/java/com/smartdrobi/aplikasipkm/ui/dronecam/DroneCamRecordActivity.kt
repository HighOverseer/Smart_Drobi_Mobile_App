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
import com.smartdrobi.aplikasipkm.BuildConfig
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ActivityDroneCamRecordBinding
import com.smartdrobi.aplikasipkm.domain.helper.ViewRecorder
import java.io.IOException

class DroneCamRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDroneCamRecordBinding
    /*    private var job: Job?=null
        private var convertJob: Job?=null*/

    private var isRecording = false
    /*  private lateinit var encoder: BitmapVideoEncoder

      private val listBitmaps = mutableListOf<Bitmap>()*/

    private lateinit var viewRecorder: ViewRecorder

    private lateinit var workerHandler: Handler
    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDroneCamRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewDroneCam()

        /*  encoder = BitmapVideoEncoder(
              object:BitmapVideoEncoder.IBitmapToVideoEncoderCallback{
                  override fun onEncodingComplete(outputFile: File?) {
                      println(outputFile?.absolutePath)
                  }
              }
          )*/


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
                        //job?.cancel()
                        stopRecord()
                    }
                }
            }


            /*     btnRecord.setOnClickListener {
                     if (btnRecord.text == "Rekam"){
                         btnRecord.text = "Stop"
                         isRecording = true
                         job = lifecycleScope.launch {
                             while (isRecording){
                                 val bitmap = binding.viewDroneCam.drawToBitmap()
                                 listBitmaps.add(bitmap)
                                 delay(10)
                             }
                         }
                     }else{
                         btnRecord.text = "Rekam"
                         //job?.cancel()
                         isRecording = false
                         convertBitmapsToVideo()
                     }
                 }*/

            /* btnRecord.setOnClickListener {
                 if (btnRecord.text == "Rekam"){
                     btnRecord.text = "Stop"
                     isRecording = true
                     encoder.startEncoding(root.width, root.height, File(
                         Environment.getExternalStoragePublicDirectory(
                             Environment.DIRECTORY_DCIM
                         ),
                         "test.mp4"
                     ))
                     job = lifecycleScope.launch {
                         while (isRecording){
                             delay(30)
                             val bitmap = createBitmap()
                             encoder.queueFrame(bitmap)
                         }
                     }
                 }else{
                     btnRecord.text = "Rekam"
                     //job?.cancel()
                     isRecording = false
                     encoder.stopEncoding()
                 }

             }*/
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
        MediaRecorder.OnErrorListener { mr, what, extra ->
            viewRecorder.reset()
            viewRecorder.release()
        }
    }

    /*private fun convertBitmapsToVideo(){
        convertJob = lifecycleScope.launch(Dispatchers.IO){
            var isSuccess = true
            for(i in listBitmaps.indices){
                try {
                    val currBitmap = listBitmaps[i]

                    val file = File(filesDir, "bitmapImage${i.plus(1)}.png")
                    file.createNewFile()

                    currBitmap.saveToFile(file)
                }catch (e:Exception){
                    isSuccess = false

                }
            }

            if (isSuccess){
                val videoFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "test.mp4"
                )
                if (videoFile.exists()){
                    videoFile.delete()
                }

                FFmpegKit.executeAsync(
                    "-f image2 -i ${filesDir.absolutePath}/bitmapImage%d.png ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/test.mp4"
                ) {
                    filesDir.listFiles()?.forEach {
                        it.delete()
                    }
                    println("Success")
                }
            }else{
                filesDir.listFiles()?.forEach {
                    it.delete()
                }
                *//*Toast.makeText(
                    this@DroneCamRecordActivity,
                    "Theres some mistake..",
                    Toast.LENGTH_SHORT
                ).show()*//*
            }
        }
    }*/

    /*    private suspend fun Bitmap.saveToFile(file:File) = withContext(Dispatchers.IO){
            val bos = ByteArrayOutputStream()
            compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        }*/

    /*    private fun createBitmap():Bitmap{
            val bitmap = Bitmap.createBitmap(
                binding.viewDroneCam.width,
                binding.viewDroneCam.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawColor(
                Color.TRANSPARENT,
                PorterDuff.Mode.CLEAR
            )
            binding.viewDroneCam.draw(canvas)
            return bitmap
        }*/

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
                setUrl(BuildConfig.DRONE_CAM_URL)
            }
        }
    }

    /*    private fun getVideoFile(): File {
            return File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM
                ),
                "test.mp4"
            )
        }*/

    private fun isInPortraitMode(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun onResume() {
        binding.viewDroneCam.startStream()
        super.onResume()
    }

    override fun onPause() {
        //encoder.abortEncoding()
        binding.viewDroneCam.stopStream()
        super.onPause()
    }

    override fun onStop() {
        binding.viewDroneCam.stopStream()
        super.onStop()
    }

}