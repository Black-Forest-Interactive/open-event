package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.infrastructure.cache.CacheInfo
import io.micronaut.security.authentication.Authentication

interface CacheAPI {
    companion object {
        const val PERMISSION_READ = "openevent.cache.read"
        const val PERMISSION_WRITE = "openevent.cache.write"
    }

    fun get(auth: Authentication, key: String): CacheInfo?
    fun getAll(auth: Authentication): List<CacheInfo>

    fun reset(auth: Authentication, key: String): CacheInfo?
    fun resetAll(auth: Authentication): List<CacheInfo>


}
