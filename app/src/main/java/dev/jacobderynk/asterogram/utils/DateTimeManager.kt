package dev.jacobderynk.asterogram.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeManager {

    fun formatResponseDatetime(datetime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd. MMMM, yyyy", Locale.getDefault())

            val date = inputFormat.parse(datetime) ?: ""
            outputFormat.format(date)
        } catch (e: Exception) {
            Timber.e(e, "formatResponseDatetime exception")
            datetime
        }
    }

    fun getYearFromResponseDatetime(datetime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())

            val date = inputFormat.parse(datetime) ?: ""
            outputFormat.format(date)
        } catch (e: Exception) {
            Timber.e(e, "formatResponseDatetime exception")
            datetime
        }
    }
}