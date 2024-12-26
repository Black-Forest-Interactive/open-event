package de.sambalmueslie.openevent.core.search.account

import com.jillesvangurp.searchdsls.mappingdsl.FieldMappings
import de.sambalmueslie.openevent.core.search.common.FieldMappingProvider
import jakarta.inject.Singleton

@Singleton
class AccountFieldMappingProvider : FieldMappingProvider {
    override fun createMappings(): FieldMappings.() -> Unit {
        return {
            number<Long>("id")

            text("name")
            text("email")

            text("firstName")
            text("lastName")
        }
    }
}