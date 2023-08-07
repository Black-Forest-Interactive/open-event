package de.sambalmueslie.openevent.storage

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.data.model.Page


class SimpleDataObjectConverter<T : BusinessObject<*>, O : SimpleDataObject<T>> : DataObjectConverter<T, O> {
    override fun convert(obj: O): T {
        return obj.convert()
    }

    override fun convert(objs: List<O>): List<T> {
       return objs.map { it.convert() }
    }

    override fun convert(page: Page<O>): Page<T> {
        return page.map { it.convert() }
    }
}
