package de.sambalmueslie.openevent.core.search.category

import de.sambalmueslie.openevent.core.category.api.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategorySearchEntryData(
    var id: String,
    var name: String,
) {
    companion object {


        fun create(obj: Category): CategorySearchEntryData {
            return CategorySearchEntryData(
                obj.id.toString(),
                obj.name
            )
        }
    }
}
