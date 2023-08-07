package de.sambalmueslie.openevent.infrastructure.mail.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailJobContent
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "MailJobContent")
@Table(name = "mail_job_content")
data class MailJobContentData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    @Column var mailJson: String,
    @Column var fromJson: String,
    @Column var toJson: String,
    @Column var bccJson: String,
    @Column(unique = true) var jobId: Long,
) : SimpleDataObject<MailJobContent> {


    companion object {
        private val mapper = ObjectMapper()

        fun create(
            mail: Mail,
            from: MailParticipant,
            to: List<MailParticipant>,
            bcc: List<MailParticipant>,
            jobId: Long
        ): MailJobContentData {
            val m = mapper.writeValueAsString(mail)
            val f = mapper.writeValueAsString(from)
            val t = mapper.writeValueAsString(to)
            val b = mapper.writeValueAsString(bcc)
            val data = MailJobContentData(0, m, f, t, b, jobId)
            data.mail = mail
            data.from = from
            data.to = to
            data.bcc = bcc
            return data
        }
    }

    override fun convert(): MailJobContent {
        return MailJobContent(id, getMailObj(), getFromObj(), getToObj(), getBccObj())
    }

    @Transient
    private var mail: Mail? = null

    @Transient
    fun getMailObj(): Mail {
        if (mail == null) {
            mail = mapper.readValue<Mail>(mailJson)
        }
        return mail!!
    }

    @Transient
    private var from: MailParticipant? = null

    @Transient
    fun getFromObj(): MailParticipant {
        if (from == null) {
            from = mapper.readValue<MailParticipant>(fromJson)
        }
        return from!!
    }

    @Transient
    private var to: List<MailParticipant>? = null

    @Transient
    fun getToObj(): List<MailParticipant> {
        if (to == null) {
            to = mapper.readValue<List<MailParticipant>>(toJson)
        }
        return to ?: emptyList()
    }

    @Transient
    private var bcc: List<MailParticipant>? = null

    @Transient
    fun getBccObj(): List<MailParticipant> {
        if (bcc == null) {
            bcc = mapper.readValue<List<MailParticipant>>(bccJson)
        }
        return bcc ?: emptyList()
    }


}
