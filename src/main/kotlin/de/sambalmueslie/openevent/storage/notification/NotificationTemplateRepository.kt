package de.sambalmueslie.openevent.storage.notification

import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect


@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface NotificationTemplateRepository : DataObjectRepository<Long, NotificationTemplateData> {

    fun findByTypeId(typeId: Long, pageable: Pageable): Page<NotificationTemplateData>
    fun findOneByTypeIdAndLang(typeId: Long, lang: String): NotificationTemplateData?
}
