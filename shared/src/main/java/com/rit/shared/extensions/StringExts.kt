package com.rit.shared.extensions

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.math.RoundingMode
import java.nio.charset.StandardCharsets
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Chan Myae Aung on 3/30/21.
 */
const val TAG_ERROR_DIALOG = "ErrorDialog"

@Throws(UnsupportedEncodingException::class)
fun String.decode(): String {
    val decodedBytes = Base64.decode(this, Base64.DEFAULT)
    return String(decodedBytes, StandardCharsets.UTF_8)
}

fun String.prefixBearer(): String {
    return if (this.contains("bearer", true)) {
        this
    } else {
        "Bearer $this"
    }
}

fun String.asFormatter(locale: Locale): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(this, locale)
}

fun String.prefixPlusSign(): String {
    return if (this.startsWith("+")) this else "+$this"
}

fun String?.orNAValue(): String {
    return this ?: "N/A"
}

fun String.formatHour(): String {
    val code24Hours = SimpleDateFormat("HH", Locale.US) // 24 hour format

    val dateCode12 = try {
        code24Hours.parse(this) // 24 hour
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    val hour = code24Hours.format(dateCode12).toInt() // 24

    return if (hour < 12) {
        "$hour am"
    } else if (hour == 12) {
        "12 pm"
    } else {
        "${hour - 12} pm"
    }
}

fun String.formatHourIn24HrFormat(): Int {
    val code24Hours = SimpleDateFormat("HH", Locale.US) // 24 hour format

    val dateCode12 = try {
        code24Hours.parse(this) // 24 hour
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return code24Hours.format(dateCode12).toInt() // 24
}

fun String.formatMinIn24HrFormat(): Int {
    val code24Hours = SimpleDateFormat("MM", Locale.US) // 24 hour format

    val dateCode12 = try {
        code24Hours.parse(this) // 24 hour
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return code24Hours.format(dateCode12).toInt() // 24
}

fun String.formatHourMin(): String {
    if (this.isBlank()) return ""
    val code12Hours = SimpleDateFormat("hh:mm") // 12 hour format
    val formatTwelve: String

    val dateCode12 = try {
        code12Hours.parse(this) // 12 hour
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    formatTwelve = code12Hours.format(dateCode12) // 12

    if (formatTwelve.contains("12")) return "$formatTwelve pm"
    return if (formatTwelve == this) {
        "$formatTwelve am"
    } else {
        "$formatTwelve pm"
    }
}

fun Double.asDistanceFormat(): String {
    return "${DecimalFormat("###0").format(this)}m away"
}

fun String.distanceFormat(): String {

    val distance = this.toInt()
    return if (distance >= 1000) {
        val kmDistance = distance / 1000.00f
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        df.format(kmDistance)
        "${df.format(kmDistance)}km away"
    } else {
        "${distance}m away"
    }
}

fun String.isAfter(lastTime: String?): Boolean {
    var currMin = 0
    var lastTimingMin = 0
    try {
        this.trim().split(":").forEachIndexed { index, s ->
            currMin +=
                if (index == 0)
                    s.toInt() * 60
                else
                    s.toInt()
        }
        lastTime?.trim()?.split(":")?.forEachIndexed { index, s ->
            lastTimingMin +=
                if (index == 0)
                    s.toInt() * 60
                else
                    s.toInt()
        }
    } catch (e: Exception) {
    }
    return currMin > lastTimingMin
}
