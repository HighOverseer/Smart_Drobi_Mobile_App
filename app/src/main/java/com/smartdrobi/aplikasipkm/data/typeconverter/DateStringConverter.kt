package com.smartdrobi.aplikasipkm.data.typeconverter

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import androidx.room.TypeConverter
import com.smartdrobi.aplikasipkm.domain.helper.DATE_FORMAT_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class DateStringConverter {
    private val format = SimpleDateFormat(DATE_FORMAT_PATTERN, ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0])

    @TypeConverter
    fun dateToString(date:Date):String{
        return format.format(date)
    }
    @TypeConverter
    fun stringToDate(value:String):Date?{
        return try {
            format.parse(value)
        }catch (e:ParseException){
            null
        }
    }
}