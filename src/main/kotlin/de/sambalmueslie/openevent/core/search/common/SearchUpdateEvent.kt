package de.sambalmueslie.openevent.core.search.common

data class SearchUpdateEvent<T>(
    val data: T,
    val type: ChangeType
)
