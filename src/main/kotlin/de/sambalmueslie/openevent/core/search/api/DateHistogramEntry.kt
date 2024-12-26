package de.sambalmueslie.openevent.core.search.api

import java.time.LocalDate

data class DateHistogramEntry(
    val date: LocalDate,
    val amount: Long
)
