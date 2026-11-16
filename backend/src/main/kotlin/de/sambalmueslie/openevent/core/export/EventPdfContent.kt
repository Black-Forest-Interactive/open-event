package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

data class EventPdfContent(
    val event: Event,
    val location: Location,
    val registration: RegistrationInfo,
    val categories: List<Category>,
    val audiences: List<Audience>,
    val qrCode: String,
    val availableSpace: List<Char>,
    val description: String,
    val shortDescription: String,
    val url: String
) {
    companion object {
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        private val dayFormatter = DateTimeFormatter.ofPattern("EEE, d. MMM", Locale.GERMAN)

        /* accent + tint per category, stable via name hash */
        private val palette = listOf(
            Pair("#059669", "#ecfdf5"),
            Pair("#4f46e5", "#eef2ff"),
            Pair("#d97706", "#fffbeb"),
            Pair("#e11d48", "#fff1f2"),
            Pair("#0284c7", "#f0f9ff"),
            Pair("#7c3aed", "#f5f3ff")
        )
    }

    val weekday: String = event.start.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMAN)

    val categoryLine: String = categories.joinToString(" · ") { it.name }

    val dateLine: String = if (event.start.toLocalDate() == event.finish.toLocalDate()) {
        "${dayFormatter.format(event.start)} · ${timeFormatter.format(event.start)}–${timeFormatter.format(event.finish)}"
    } else {
        event.format()
    }

    val ownerName: String = listOf(event.owner.firstName, event.owner.lastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { event.owner.name }

    val ownerInitials: String = ownerName.split(" ", "-")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    val urlShort: String = url.substringBefore("?").removePrefix("https://").removePrefix("http://").removeSuffix("/")

    private val accent = palette[Math.floorMod((categories.firstOrNull()?.name ?: "").hashCode(), palette.size)]
    val accentColor: String = accent.first
    val accentTint: String = accent.second
}
