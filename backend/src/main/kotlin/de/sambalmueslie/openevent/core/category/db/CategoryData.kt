package de.sambalmueslie.openevent.core.category.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Category")
@Table(name = "category")
data class CategoryData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var name: String = "",
    @Column var iconUrl: String = "",

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Category> {
    companion object {
        fun create(
            request: CategoryChangeRequest,
            timestamp: LocalDateTime
        ): CategoryData {
            return CategoryData(0, request.name, request.iconUrl, timestamp)
        }
    }

    override fun convert(): Category {
        return Category(id, name, iconUrl)
    }

    fun update(request: CategoryChangeRequest, timestamp: LocalDateTime): CategoryData {
        name = request.name
        iconUrl = request.iconUrl
        updated = timestamp
        return this
    }
}

