package de.sambalmueslie.openevent.core

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val dateFormatter = DateTimeFormatter.ofPattern("dd. MMMM. YYYY", Locale.GERMAN)
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN)
private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd. MMMM. YYYY HH:mm", Locale.GERMAN)

fun formatTimestamp(timestamp: LocalDateTime) = "${dateTimeFormatter.format(timestamp)}"

fun formatRange(start: LocalDateTime, finish: LocalDateTime): String {
    val startDate = start.toLocalDate()
    val endDate = start.toLocalDate()
    val startTime = start.toLocalTime()
    val endTime = finish.toLocalTime()

    // 22. Okt. 2020 18:00 - 22:00 Uhr
    if (startDate == endDate) {
        return "${dateFormatter.format(startDate)} ${timeFormatter.format(startTime)} - ${
            timeFormatter.format(
                endTime
            )
        }"
    }
    return "${dateFormatter.format(startDate)} ${timeFormatter.format(startTime)} - " +
            "${dateFormatter.format(endDate)} ${timeFormatter.format(endTime)}"
}