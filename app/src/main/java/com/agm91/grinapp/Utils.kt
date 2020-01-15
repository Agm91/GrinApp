package com.agm91.grinapp

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun stringToDate(date: String): Date {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            return simpleDateFormat.parse(date)
        }
    }
}