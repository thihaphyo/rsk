package com.rit.shared.formatter

import android.text.format.DateFormat
import java.util.Locale
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Chan Myae Aung on 3/30/21.
 */
class DateFormatter {

    fun changeFormat(
        date: String,
        originPattern: String,
        targetPattern: String,
        locale: Locale
    ): String {
        val pattern = if (locale == Locale.SIMPLIFIED_CHINESE) {
            DateFormat.getBestDateTimePattern(locale, targetPattern)
        } else {
            targetPattern
        }
        return try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(originPattern))
                .format(DateTimeFormatter.ofPattern(pattern, locale))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun changeZonedFormat(date: String, targetPattern: String, locale: Locale): String {
        val pattern = if (locale == Locale.SIMPLIFIED_CHINESE) {
            DateFormat.getBestDateTimePattern(locale, targetPattern)
        } else {
            targetPattern
        }
        return try {
            ZonedDateTime.parse(date)
                .withZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(pattern, locale))
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(date)
                    .format(DateTimeFormatter.ofPattern(pattern, locale))
            } catch (e: Exception) {
                ""
            }
        }
    }
}
