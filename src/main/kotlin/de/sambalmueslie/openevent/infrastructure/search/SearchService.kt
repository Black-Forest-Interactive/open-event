package de.sambalmueslie.openevent.infrastructure.search

interface SearchService {

    fun <T> getClient(name: String): SearchClient<T>
}
