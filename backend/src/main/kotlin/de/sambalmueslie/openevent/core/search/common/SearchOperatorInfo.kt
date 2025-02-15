package de.sambalmueslie.openevent.core.search.common

data class SearchOperatorInfo(
    val key: String,
    val status: SearchOperatorStatus,
    val statistics: SearchOperatorStats
)
