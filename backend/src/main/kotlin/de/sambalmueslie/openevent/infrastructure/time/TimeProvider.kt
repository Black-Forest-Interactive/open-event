package de.sambalmueslie.openevent.infrastructure.time

import java.time.LocalDateTime

fun interface TimeProvider {
    fun now(): LocalDateTime
}
