package com.example.models.models

import java.util.Calendar
import java.util.concurrent.TimeUnit

class Utils {

    companion object {
        fun getDifferenceInHours(unixTime: Double): Long {
            val givenCalendar = Calendar.getInstance().apply {
                timeInMillis = unixTime.toLong() * 1000
            }
            val currentCalendar = Calendar.getInstance()
            val diff = currentCalendar.timeInMillis - givenCalendar.timeInMillis
            return TimeUnit.MILLISECONDS.toHours(diff)
        }
    }
}
