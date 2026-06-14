package de.sambalmueslie.openevent.core.link.db

import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.link.api.Link
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class LinkConverter() : DataObjectConverter<Link, LinkData> {
    override fun convert(obj: LinkData): Link {
        return obj.convert()
    }

    override fun convert(objs: List<LinkData>): List<Link> {
        return objs.map { convert(it) }
    }

    override fun convert(page: Page<LinkData>): Page<Link> {
        return page.map { convert(it) }
    }

}