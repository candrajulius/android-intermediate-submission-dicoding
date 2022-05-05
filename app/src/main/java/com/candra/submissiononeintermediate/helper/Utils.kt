package com.candra.submissiononeintermediate.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import android.text.format.DateFormat
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.candra.submissiononeintermediate.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.lang.NullPointerException
import java.util.*


private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val YEARFORMAT = "yyyy"

val timeStampt: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength:Int
    do{
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,compressQuality,bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    }while (streamLength > 1000000)

    bitmap.compress(Bitmap.CompressFormat.JPEG,compressQuality, FileOutputStream(file))
    return file
}

fun uriToFile(selectImg: Uri, context: Context): File{
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTemptFile(context)

    val inputStream = contentResolver.openInputStream(selectImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf,0,len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun createCustomTemptFile(context: Context): File{
    val storage: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStampt,".jpg",storage)
}

fun yearFormat(): String{
    return SimpleDateFormat(YEARFORMAT, Locale.getDefault()).format(System.currentTimeMillis())
}

val String.genereteDate
    get() : String {
        val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = apiFormat.parse(this.split("T").first())
        return DateFormat.format("EEEE, dd MMMM yyy", date).toString()
    }

fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int,context:Context): BitmapDescriptor {
    val vectorDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
    if (vectorDrawable == null) {
        Log.e("BitmapHelper", "Resource not found")
        return BitmapDescriptorFactory.defaultMarker()
    }
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    DrawableCompat.setTint(vectorDrawable, color)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun convertLatLngToAddress(lat: Double,lon: Double,context: Context): String{
    var data = ""
    Geocoder(context).apply {
        getFromLocation(lat,lon,1).also { alamat ->
            val address = alamat[0].getAddressLine(0)
            data = "Alamat: $address"
        }
    }
    return  data
}

fun convertLatLngToAddressForAdapter(lat: Double,lon: Double,context: Context): String{
    var message = ""
    if (lat != 0.0 && lon != 0.0 ) {
        if (lat >= -90 && lat <= 90) {
            val geocoder = Geocoder(context)
            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            if (addresses.isNotEmpty()) {
                try {
                   message = buildString {
                       append(addresses[0].locality).append(",").append(addresses[0].subAdminArea)
                   }
                }catch (e: NullPointerException){
                    Log.d("TAG", "convertLatLngToAddressForAdapter: ${e.message.toString()}")
                }
            } else message = "0.0"
        }else{
           message =  "Illegal Argumen Exception"
        }
    }else{
       message =  context.getString(R.string.lokas_not_found)
    }
    return message
}