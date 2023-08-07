package de.sambalmueslie.openevent.infrastructure.mail.db

import de.sambalmueslie.openevent.infrastructure.mail.api.MailJobHistoryEntry
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "MailJobHistory")
@Table(name = "mail_job_history")
data class MailJobHistoryEntryData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    @Column var message: String,
    @Column var timestamp: LocalDateTime,
    @Column var jobId: Long
) : SimpleDataObject<MailJobHistoryEntry> {
    override fun convert(): MailJobHistoryEntry {
        return MailJobHistoryEntry(id, message, timestamp)
    }
}
