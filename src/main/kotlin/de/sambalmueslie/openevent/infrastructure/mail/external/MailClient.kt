package de.sambalmueslie.openevent.infrastructure.mail.external

import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant

interface MailClient {
    fun send(
        mail: Mail,
        from: MailParticipant,
        to: List<MailParticipant>,
        bcc: List<MailParticipant> = emptyList()
    ): Boolean
}
