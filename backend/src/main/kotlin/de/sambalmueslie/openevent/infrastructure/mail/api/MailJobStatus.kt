package de.sambalmueslie.openevent.infrastructure.mail.api

enum class MailJobStatus {
    QUEUED,
    RETRY,
    FINISHED,
    FAILED
}
