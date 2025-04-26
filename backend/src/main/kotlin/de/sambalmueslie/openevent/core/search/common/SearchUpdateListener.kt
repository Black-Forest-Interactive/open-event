package de.sambalmueslie.openevent.core.search.common

interface SearchUpdateListener<T> {
    fun updateSearch(event: SearchUpdateEvent<T>)
}