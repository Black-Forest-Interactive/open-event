package de.sambalmueslie.openevent.common.time

import java.time.LocalDateTime

interface TimeProvider {
    fun now(): LocalDateTime
}
