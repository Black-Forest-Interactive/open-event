package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.finder.api.Finder
import de.sambalmueslie.openevent.core.finder.api.FinderAnswer
import de.sambalmueslie.openevent.core.finder.api.FinderChangeRequest
import de.sambalmueslie.openevent.core.finder.api.FinderEntry
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface FinderStorage : Storage<Long, Finder, FinderChangeRequest> {
    fun create(request: FinderChangeRequest, owner: Account): Finder

    fun updateEntry(finder: Finder, event: Event, answer: FinderAnswer): FinderEntry
    fun deleteEntry(finder: Finder, event: Event)
    fun deleteEntries(finder: Finder)

    fun getEntries(finder: Finder): List<FinderEntry>

    fun deleteByEvent(event: Event)

    fun findByOwner(owner: Account, pageable: Pageable): Page<Finder>

}