package com.android.weatherapp.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class Helper {
    fun getDayName(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    fun getHour(time: String):String {
        return when(time){
            "0"-> "OO:00 AM"
            "300"-> "O3:00 AM"
            "600"-> "O6:00 AM"
            "900"-> "O9:00 AM"
            "1200"-> "12:00 PM"
            "1500"-> "15:00 PM"
            "1800"-> "18:00 PM"
            "2100"-> "21:00 PM"
            else -> "OO:00 AM"
        }

    }
}