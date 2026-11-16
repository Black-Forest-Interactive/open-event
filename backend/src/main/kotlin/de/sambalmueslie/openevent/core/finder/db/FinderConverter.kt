package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.finder.api.Finder
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class FinderConverter : DataObjectConverter<Finder, FinderData> {
    override fun convert(obj: FinderData): Finder {
        return Finder(obj.id, obj.ownerId, obj.name, obj.request, obj.updated ?: obj.created)
    }

    override fun convert(objs: List<FinderData>): List<Finder> {
        return objs.map { convert(it) }
    }

    override fun convert(page: Page<FinderData>): Page<Finder> {
        return page.map { convert(it) }
    }
}
