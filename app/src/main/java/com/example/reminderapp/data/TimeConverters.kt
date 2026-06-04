package com.example.reminderapp.data

import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromString(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it, formatter) }
    }

    @TypeConverter
    fun toString(time: LocalTime?): String? {
        return time?.format(formatter)
    }
}
