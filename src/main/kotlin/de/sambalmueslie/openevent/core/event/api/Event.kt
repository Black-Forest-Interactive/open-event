package de.sambalmueslie.openevent.core.event.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serdeable
data class Event(
    override val id: Long,

    val owner: AccountInfo,

    val start: LocalDateTime,
    val finish: LocalDateTime,

    val title: String,
    val shortText: String,
    val longText: String,
    val imageUrl: String,
    val iconUrl: String,

    val hasLocation: Boolean,
    val hasRegistration: Boolean,
    val published: Boolean,

    val tags: Set<String>,

    val created: LocalDateTime,
    val changed: LocalDateTime?
) : BusinessObject<Long> {
    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("dd. MMMM. YYYY", Locale.GERMAN)
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN)
    }

    fun format(): String {
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
}
