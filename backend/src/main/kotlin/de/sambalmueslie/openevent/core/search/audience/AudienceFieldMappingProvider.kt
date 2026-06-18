package de.sambalmueslie.openevent.core.search.audience

import com.jillesvangurp.searchdsls.mappingdsl.FieldMappings
import de.sambalmueslie.openevent.core.search.common.FieldMappingProvider
import jakarta.inject.Singleton

@Singleton
class AudienceFieldMappingProvider : FieldMappingProvider {
    override fun createMappings(): FieldMappings.() -> Unit {
        return {
            number<Long>("id")
            text("name")
        }
    }
}