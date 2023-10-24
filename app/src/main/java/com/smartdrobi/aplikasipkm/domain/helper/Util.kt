package com.smartdrobi.aplikasipkm.domain.helper

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.ListPopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.ConfigurationCompat
import com.bumptech.glide.Glide
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.domain.model.BridgeCheckField
import com.smartdrobi.aplikasipkm.ui.adapter.SpAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

const val JAKARTA_LAT = -6.2293796
const val JAKARTA_LON = 106.6647046
const val AUTHORITY = "com.smartdrobi.aplikasipkm"

fun ImageView.loadImage(context: Context, imageUrl:String){
    Glide.with(context)
        .load(imageUrl)
        .into(this)
}

private var numOfPhotos = 0
private const val PHOTO = "photo_"
fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("$PHOTO${numOfPhotos + 1}", ".jpg", storageDir)
}

fun AppCompatSpinner.setInit(context: Context,listItems:List<String>){
    val adapter = SpAdapter(context, listItems)
    setScrollable()
    this.adapter = adapter
}

fun Float.toDp(displayMetrics: DisplayMetrics): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        displayMetrics
    )
}

fun showDialogIntentPhoto(
    context: Context,
    openGallery:(Int, Int) -> Unit,
    openCameraDrone:(Int, Int) -> Unit,
    openCamera:(Int, Int) -> Unit,
    fieldPosition:Int,
    parentFieldPosition:Int = -1
){
    AlertDialog.Builder(context)
        .setTitle("Pilih Metode")
        .setItems(context.resources.getStringArray(R.array.intent_photo)){ dialog, which ->
            when (which) {
                0 -> openGallery(fieldPosition, parentFieldPosition)
                1 -> openCameraDrone(fieldPosition, parentFieldPosition)
                2 -> openCamera(fieldPosition, parentFieldPosition)
            }
            dialog.dismiss()
        }
        .create()
        .show()
}

private fun AppCompatSpinner.setScrollable(){
    val popupField = AppCompatSpinner::class.java.getDeclaredField("mPopup")
    popupField.isAccessible = true

    val popUpWindow = popupField.get(this) as ListPopupWindow
    val displayMetrics = this.context.resources.displayMetrics
    popUpWindow.height = 100.toDp(displayMetrics)

}

fun ImageView.loadImage(context: Context, imageRestId:Int){
    Glide.with(context)
        .load(imageRestId)
        .into(this)
}

fun ImageView.loadImage(context:Context, imageBitmap: Bitmap){
    Glide.with(context)
        .load(imageBitmap)
        .into(this)
}

fun ImageView.loadImage(context:Context, imageFile: File){
    Glide.with(context)
        .load(imageFile)
        .into(this)
}


fun showToast(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Int.toDp(displayMetrics: DisplayMetrics):Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics).toInt()
}



fun String.toDate(format: String): Date {
    val simpleDateFormat = SimpleDateFormat(format, ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    return try {
        simpleDateFormat.parse(this) as Date
    }catch (e:Exception){
        getCurrentDate()
    }
}

fun Date.toString(format: String):String{
    val simpleDateFormat = SimpleDateFormat(format, ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    return try {
        simpleDateFormat.format(this)
    }catch (e:Exception){
        e.printStackTrace()
        getCurrentDateInString("yyyy-MM-dd")!!
    }
}

fun getCurrentDateInString(format:String):String?{
    val simpleDateFormat = SimpleDateFormat(format, ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])
    val currentDate = getCurrentDate()
    return try {
        simpleDateFormat.format(currentDate)
    }catch (e:Exception){
        null
    }
}

fun getCurrentDate():Date = Calendar.getInstance().time

fun countSpanImageCollection(imageCount:Int, columnCount:Int):Int{
    return (imageCount/columnCount)+1
}

fun View.setMargin(
    marginTop:Int = -1,
    marginBottom:Int = -1,
    marginEnd:Int = -1,
    marginStart: Int = -1
){
    val displayMetrics = this.resources.displayMetrics
    val margins = listOf(
        marginTop,
        marginBottom,
        marginEnd,
        marginStart
    )

    val params = this.layoutParams as ConstraintLayout.LayoutParams
    for (i in margins.indices){
        val currMargin = margins[i]
        if (currMargin != -1){
            when(i){
                0 ->  params.topMargin = marginTop.toDp(displayMetrics)
                1 -> params.bottomMargin = marginBottom.toDp(displayMetrics)
                2 -> params.marginEnd = marginEnd.toDp(displayMetrics)
                3 -> params.marginStart = marginStart.toDp(displayMetrics)
            }
        }
    }
    this.layoutParams = params

}

fun AppCompatEditText.setInputType(inputType: BridgeCheckField.EditTextInputType){
    when(inputType){
        BridgeCheckField.EditTextInputType.TEXT ->{
            setInputType(InputType.TYPE_CLASS_TEXT)
        }
        BridgeCheckField.EditTextInputType.NUMBER ->{
            setInputType(InputType.TYPE_CLASS_NUMBER)
        }
        BridgeCheckField.EditTextInputType.NUMBER_DECIMAL ->{
            setInputType(
                (InputType.TYPE_NUMBER_FLAG_DECIMAL.or(InputType.TYPE_CLASS_NUMBER))
            )

        }
    }
}

fun getDrawable(resources: Resources, drawableId:Int):Drawable?{
    try {
        return ResourcesCompat.getDrawable(resources, drawableId, null)
    }catch (e:Exception) {
        e.printStackTrace()
    }
    return null
}

suspend fun Bitmap.saveToFile(
    context: Context
):File = withContext(Dispatchers.IO){
    val file:File = createNewFileInFileDir(
        context,
        "Smart_Drobi-(Drone_Cam)", ".png"
    )

    try {
        val bos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }catch (e:Exception){
        e.printStackTrace()
    }
    file
}

fun createNewFileInDownloadDir(
    fileName: String,
    suffix:String = "",
): File {

    fun createFile(
        fileNameWithSuffix:String
    ):File{
        return  File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ),
            fileNameWithSuffix
        )
    }

    var file = createFile("$fileName${suffix.trim()}")
    if (!file.exists()){
        return file
    }
    if (suffix.isNotBlank()){
        for(i in 1..Int.MAX_VALUE){
            file = createFile("$fileName ($i)$suffix")
            if (!file.exists()){
                return file
            }
        }
    }else {
        for(i in 1..Int.MAX_VALUE){
            file = createFile("$fileName ($i)")
            if (!file.exists()){
                return file
            }
        }
    }
    throw (Exception("Maaf ada kesalahaan pada pembuatan file.."))
}


fun createNewFileInFileDir(
    context: Context,
    fileName: String,
    suffix:String = "",
): File {

    fun createFile(
        fileNameWithSuffix:String
    ):File{
        return  File(
            context.filesDir,
            fileNameWithSuffix
        )
    }

    var file = createFile("$fileName${suffix.trim()}")
    if (!file.exists()){
        return file
    }
    if (suffix.isNotBlank()){
        for(i in 1..Int.MAX_VALUE){
            file = createFile("$fileName ($i)$suffix")
            if (!file.exists()){
                return file
            }
        }
    }else {
        for(i in 1..Int.MAX_VALUE){
            file = createFile("$fileName ($i)")
            if (!file.exists()){
                return file
            }
        }
    }
    throw (Exception("Maaf ada kesalahaan pada pembuatan file.."))
}


suspend fun uriToFile(selectedImg: Uri, context: Context): File? = withContext(Dispatchers.IO){
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)
    val outputStream = FileOutputStream(myFile)
    try {
        val inputStream = contentResolver.openInputStream(selectedImg)
        if (inputStream != null) {
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.flush()
            outputStream.close()
            inputStream.close()
            return@withContext myFile
        }else{
            outputStream.flush()
            outputStream.close()
        }
    }catch (e:Exception) {
        if (myFile.exists()) myFile.delete()

        outputStream.flush()
        outputStream.close()

    }
    null
}

inline fun showDialogConfirmation(
    context: Context,
    message:String,
    crossinline postiveBtnAction:()->Unit,
    crossinline negativeBtnAction:()->Unit = {}
){
    AlertDialog.Builder(context)
        .setTitle("Konfirmasi")
        .setMessage(message)
        .setPositiveButton("Iya"){ dialog, _ ->
            dialog.dismiss()
            postiveBtnAction()
            /*dialog.dismiss()*/
        }
        .setNegativeButton("Tidak"){ dialog, _ ->
            negativeBtnAction()
            dialog.cancel()
        }
        .create()
        .show()
}