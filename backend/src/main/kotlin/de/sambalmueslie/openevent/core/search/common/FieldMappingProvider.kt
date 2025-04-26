package de.sambalmueslie.openevent.core.search.common

import com.jillesvangurp.searchdsls.mappingdsl.FieldMappings

interface FieldMappingProvider {
    fun createMappings(): FieldMappings.() -> Unit
}