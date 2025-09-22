/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object DateUtils {

    // API date formats that we might receive
    private val API_FORMATTERS = listOf(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ISO_INSTANT,
        DateTimeFormatter.RFC_1123_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )

    // Display formatters
    private val DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
    private val DISPLAY_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
    private val DISPLAY_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm", Locale.ENGLISH)
    private val SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)
    private val WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)
    private val API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

    /**
     * Parse a date string from API response with multiple fallback formats
     */
    fun parseApiDateTime(dateString: String?): LocalDateTime? {
        if (dateString.isNullOrBlank()) return null

        // Clean up common formatting issues
        val cleanedDateString = dateString.trim()
            .replace("T", "T")  // Ensure proper ISO format
            .replace(" ", "T")  // Convert space to T for ISO

        // Try each formatter in order
        for (formatter in API_FORMATTERS) {
            try {
                return when {
                    // For formatters that include timezone info
                    formatter == DateTimeFormatter.ISO_OFFSET_DATE_TIME ||
                    formatter == DateTimeFormatter.ISO_INSTANT ||
                    formatter == DateTimeFormatter.RFC_1123_DATE_TIME ||
                    cleanedDateString.contains("Z") ||
                    cleanedDateString.contains("+") ||
                    cleanedDateString.takeLast(6).contains("-") -> {
                        val zonedDateTime = ZonedDateTime.parse(cleanedDateString, formatter)
                        zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                    }
                    // For date-only formats
                    formatter == DateTimeFormatter.ofPattern("yyyy-MM-dd") -> {
                        LocalDate.parse(cleanedDateString, formatter).atStartOfDay()
                    }
                    // For local datetime formats
                    else -> {
                        LocalDateTime.parse(cleanedDateString, formatter)
                    }
                }
            } catch (e: DateTimeParseException) {
                // Continue to next formatter
                continue
            }
        }

        // If all parsing attempts fail, log the issue and return null instead of current time
        println("DateUtils: Failed to parse date string: '$dateString'")
        return null
    }

    /**
     * Parse a date string and return current time as fallback (for backward compatibility)
     */
    fun parseApiDateTimeWithFallback(dateString: String?): LocalDateTime {
        return parseApiDateTime(dateString) ?: LocalDateTime.now()
    }

    /**
     * Format LocalDateTime for display to user
     */
    fun formatDisplayDate(dateTime: LocalDateTime?): String {
        if (dateTime == null) return "TBD"
        return dateTime.format(DISPLAY_DATE_FORMATTER)
    }

    /**
     * Format LocalDateTime for time display
     */
    fun formatDisplayTime(dateTime: LocalDateTime?): String {
        if (dateTime == null) return "TBD"
        return dateTime.format(DISPLAY_TIME_FORMATTER)
    }

    /**
     * Format LocalDateTime for full date and time display
     */
    fun formatDisplayDateTime(dateTime: LocalDateTime?): String {
        if (dateTime == null) return "TBD"
        return dateTime.format(DISPLAY_DATETIME_FORMATTER)
    }

    /**
     * Format LocalDateTime for short date display (e.g., "15 Jan")
     */
    fun formatShortDate(dateTime: LocalDateTime?): String {
        if (dateTime == null) return "TBD"
        return dateTime.format(SHORT_DATE_FORMATTER)
    }

    /**
     * Format LocalDateTime for weekday display (e.g., "Mon")
     */
    fun formatWeekday(dateTime: LocalDateTime?): String {
        if (dateTime == null) return "TBD"

        val today = LocalDate.now()
        val dateOnly = dateTime.toLocalDate()

        return when {
            dateOnly == today -> "Today"
            dateOnly == today.plusDays(1) -> "Tomorrow"
            dateOnly == today.minusDays(1) -> "Yesterday"
            else -> dateTime.format(WEEKDAY_FORMATTER)
        }
    }

    /**
     * Format LocalDate for API requests
     */
    fun formatApiDate(date: LocalDate?): String {
        if (date == null) return LocalDate.now().format(API_DATE_FORMATTER)
        return date.format(API_DATE_FORMATTER)
    }

    /**
     * Format LocalDateTime for API requests
     */
    fun formatApiDate(dateTime: LocalDateTime?): String {
        if (dateTime == null) return LocalDate.now().format(API_DATE_FORMATTER)
        return dateTime.toLocalDate().format(API_DATE_FORMATTER)
    }

    /**
     * Get time display for match card (handles different match states)
     */
    fun getMatchTimeDisplay(
        dateTime: LocalDateTime?,
        status: String?,
        elapsed: Int?
    ): String {
        return when {
            status == null -> "TBD"
            isLiveStatus(status) && elapsed != null -> "${elapsed}'"
            status == "HT" -> "HT"
            status == "FT" -> "FT"
            status == "NS" && dateTime != null -> formatDisplayTime(dateTime)
            status == "PST" -> "Postponed"
            status == "CANC" -> "Cancelled"
            status == "SUSP" -> "Suspended"
            status == "INT" -> "Interrupted"
            else -> status
        }
    }

    /**
     * Check if a status indicates a live match
     */
    fun isLiveStatus(status: String): Boolean {
        return status.uppercase() in listOf("LIVE", "1H", "2H", "HT", "ET", "BT", "P")
    }

    /**
     * Check if a status indicates a finished match
     */
    fun isFinishedStatus(status: String): Boolean {
        return status.uppercase() in listOf("FT", "AET", "PEN", "FT_PEN")
    }

    /**
     * Get relative time display (e.g., "2 hours ago", "In 3 hours")
     */
    fun getRelativeTimeDisplay(dateTime: LocalDateTime?): String {
        if (dateTime == null) return "Unknown time"

        val now = LocalDateTime.now()
        val diffMinutes = java.time.Duration.between(now, dateTime).toMinutes()

        return when {
            diffMinutes < -60 -> {
                val hours = (-diffMinutes / 60).toInt()
                if (hours < 24) "$hours hour${if (hours == 1) "" else "s"} ago"
                else {
                    val days = (hours / 24)
                    "$days day${if (days == 1) "" else "s"} ago"
                }
            }
            diffMinutes < 0 -> {
                val minutes = (-diffMinutes).toInt()
                "$minutes minute${if (minutes == 1) "" else "s"} ago"
            }
            diffMinutes < 60 -> {
                val minutes = diffMinutes.toInt()
                if (minutes == 0) "Now"
                else "In $minutes minute${if (minutes == 1) "" else "s"}"
            }
            else -> {
                val hours = (diffMinutes / 60).toInt()
                if (hours < 24) "In $hours hour${if (hours == 1) "" else "s"}"
                else {
                    val days = (hours / 24)
                    "In $days day${if (days == 1) "" else "s"}"
                }
            }
        }
    }

    /**
     * Check if a date is today
     */
    fun isToday(dateTime: LocalDateTime?): Boolean {
        if (dateTime == null) return false
        return dateTime.toLocalDate() == LocalDate.now()
    }

    /**
     * Check if a date is tomorrow
     */
    fun isTomorrow(dateTime: LocalDateTime?): Boolean {
        if (dateTime == null) return false
        return dateTime.toLocalDate() == LocalDate.now().plusDays(1)
    }

    /**
     * Check if a date is yesterday
     */
    fun isYesterday(dateTime: LocalDateTime?): Boolean {
        if (dateTime == null) return false
        return dateTime.toLocalDate() == LocalDate.now().minusDays(1)
    }

    /**
     * Get current timestamp in milliseconds
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Convert timestamp to LocalDateTime
     */
    fun timestampToLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )
    }

    /**
     * Convert LocalDateTime to timestamp
     */
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}