package io.github.kabirnayeem99.dumarketingadmin.common.util

import android.util.Log
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

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
            Timber.e("getDateFromString: " + e.message)
            null
        }
    }


    /**
     * Gets the AM or PM from a time value in string, which is
     * some what like this  "12:00 AM"
     *
     * @param timeStr [String] value of the time like "12:00 AM"
     *
     * @return [String] value like 'AM' or 'PM'
     */
    fun getMeridianFromStringTime(timeStr: String): String {
        return timeStr.substring(max(timeStr.length - 2, 0))
    }

    /**
     * Gets the Hour and Minute from a time value in string, which is
     * some what like this  "12:00 AM"
     *
     * @param timeStr [String] value of the time like "12:00 AM"
     *
     * @return [String] value like '12:00'
     */
    fun getHourMinuteTimeFromStringTime(timeStr: String): String {
        return timeStr.substring(0, timeStr.length - 2)
    }

    private const val TAG = "TimeUtilities"
}