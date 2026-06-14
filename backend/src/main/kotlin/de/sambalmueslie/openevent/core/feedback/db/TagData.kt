package de.sambalmueslie.openevent.core.feedback.db

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType

@TypeDef(type = DataType.JSON)
@Introspected
data class TagData(
    val tags: MutableSet<String> = mutableSetOf()
) {
    companion object {
        fun create(tags: Set<String>) = TagData(tags.toMutableSet())
    }
}
