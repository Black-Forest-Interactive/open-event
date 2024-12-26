package de.sambalmueslie.openevent.infrastructure.mail.api

interface MailSender {
    fun send(
        mail: Mail,
        from: MailParticipant,
        to: List<MailParticipant>,
        bcc: List<MailParticipant> = emptyList(),
        single: Boolean = false
    )

    fun send(mail: Mail, to: List<MailParticipant>, bcc: List<MailParticipant> = emptyList(), single: Boolean = false)

}
