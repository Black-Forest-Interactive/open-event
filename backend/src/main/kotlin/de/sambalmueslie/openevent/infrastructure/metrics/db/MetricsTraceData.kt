package de.sambalmueslie.openevent.infrastructure.metrics.db

import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsSource
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "MetricsTrace")
@Table(name = "metrics_trace")
data class MetricsTraceData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column @Enumerated(EnumType.STRING) var source: MetricsSource,
    @Column var type: String,
    @Column var action: String,
    @Column var accountExternalId: String,
    @Column var resource: Long,
    @Column var timestamp: LocalDateTime
)
