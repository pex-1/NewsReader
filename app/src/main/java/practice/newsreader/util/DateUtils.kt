package practice.newsreader.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getStringDate(publishedAt: String): String? {
        val date = getDate(publishedAt)
        if (date != null) {
            val dateFormat = SimpleDateFormat("MMM, yyyy")
            return dateFormat.format(date)
        }
        return publishedAt
    }


    fun getDate(publishedAt: String): Date? {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            formatter.timeZone = TimeZone.getTimeZone("GMT")
            return formatter.parse(publishedAt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }


    fun getTimePassed(publishedAt: String): String? {

        val articleDate = getDate(publishedAt)
        val current = Date()
        val milliseconds = current.time - articleDate!!.time
        val seconds = milliseconds / 1000
        val minutes = seconds / 60

        val day = minutes / 1440
        val hour = minutes % 1440 / 60
        val minute = minutes % 1440 % 60

        return "${hour}h ${minute}m"
    }
}