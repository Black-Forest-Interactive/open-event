package de.sambalmueslie.openevent.guild.api

import de.sambalmueslie.openevent.common.crud.BusinessObject

data class Guild(
    override val id: Long,
    val name: String,
) : BusinessObject<Long>
