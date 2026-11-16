package de.sambalmueslie.openevent.infrastructure.mail.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailJobContent
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import jakarta.persistence.*

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "MailJobContent")
@Table(name = "mail_job_content")
data class MailJobContentData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    @Column(name = "mail_json") @field:TypeDef(type = DataType.JSON) var mail: Mail,
    @Column(name = "from_json") @field:TypeDef(type = DataType.JSON) var from: MailParticipant,
    @Column(name = "to_json") @field:TypeDef(type = DataType.JSON) var to: List<MailParticipant>,
    @Column(name = "bcc_json") @field:TypeDef(type = DataType.JSON) var bcc: List<MailParticipant>,
    @Column(unique = true) var jobId: Long,
) : SimpleDataObject<MailJobContent> {

    companion object {
        fun create(
            mail: Mail,
            from: MailParticipant,
            to: List<MailParticipant>,
            bcc: List<MailParticipant>,
            jobId: Long
        ): MailJobContentData {
            return MailJobContentData(0, mail, from, to, bcc, jobId)
        }
    }

    override fun convert(): MailJobContent {
        return MailJobContent(id, mail, from, to, bcc)
    }

}
