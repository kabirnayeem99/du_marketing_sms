package io.github.kabirnayeem99.dumarketingstudent.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object TimeUtilities {

    private val calendar = Calendar.getInstance()

    /**
     * Gets the current time in dd-MM-yy format
     *
     * like 14-04-21
     */
    fun getCurrentDateInString(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.UK)
        return dateFormat.format(calendar.time)
    }

    /**
     * Gets current time in hh:mm a format
     *
     * like 12:31 PM
     */
    fun getCurrentTimeInString(): String {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)
        return timeFormat.format(calendar.time)
    }


    /**
     * Gets time in hh:mm a format
     *
     * like 12:31 PM
     */
    fun getTimeInString(time: Date): String {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)
        return timeFormat.format(time)
    }

    /**
     * Gets time from string in hh::mm format
     *
     * and returns a Date object
     *
     */
    fun getDateFromString(timeString: String): Date? {

        return try {
            val format = SimpleDateFormat("hh:mm a", Locale.UK)
            format.parse(timeString)
        } catch (e: Exception) {
            Log.e(TAG, "getDateFromString: ${e.message}")
            null
        }
    }


    fun getMeridiemFromStringTime(timeStr: String): String {
        return timeStr.substring((timeStr.length - 2).coerceAtLeast(0))
    }

    fun getHourMinuteTimeFromStringTime(timeStr: String): String {
        return timeStr.substring(0, timeStr.length - 2)
    }

    private const val TAG = "TimeUtilities"
}