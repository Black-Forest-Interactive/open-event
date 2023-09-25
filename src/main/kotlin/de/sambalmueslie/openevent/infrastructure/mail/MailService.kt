package de.sambalmueslie.openevent.infrastructure.mail


import de.sambalmueslie.openevent.config.MailConfig
import de.sambalmueslie.openevent.infrastructure.mail.api.*
import de.sambalmueslie.openevent.infrastructure.mail.db.*
import de.sambalmueslie.openevent.infrastructure.mail.external.MailClient
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.util.findByIdOrNull
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

@Singleton
class MailService(
    private val jobRepository: MailJobRepository,
    private val jobContentRepository: MailJobContentRepository,
    private val jobHistoryRepository: MailJobHistoryRepository,
    private val client: MailClient,
    private val timeProvider: TimeProvider,
    private val config: MailConfig
) : MailSender {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MailService::class.java)
        private const val MAX_RETRIES = 2
        private const val MAX_QUEUE_SIZE = 1000
    }


    private val newJobs = mutableListOf<MailSendJob>()

    @Synchronized
    override fun send(mail: Mail, to: List<MailParticipant>, bcc: List<MailParticipant>, single: Boolean) {
        val from = MailParticipant("", config.fromAddress)
        send(mail, from, to, bcc, single)
    }

    @Synchronized
    override fun send(
        mail: Mail,
        from: MailParticipant,
        to: List<MailParticipant>,
        bcc: List<MailParticipant>,
        single: Boolean
    ) {
        if (single) {
            to.forEach { send(mail, from, listOf(it), bcc) }
        } else {
            send(mail, from, to, bcc)
        }
    }


    private fun send(
        mail: Mail,
        from: MailParticipant,
        to: List<MailParticipant>,
        bcc: List<MailParticipant>,
    ) {
        val title = "${mail.subject} -> ${to.joinToString { it.address }}"
        val jobData = jobRepository.save(MailJobData.create(title, timeProvider.now()))
        val jobContent =
            jobContentRepository.save(MailJobContentData.create(mail, from, to, bcc, jobData.id)).convert()
        val job = MailSendJob(jobData.id, jobContent, jobHistoryRepository, timeProvider)
        newJobs.add(job)
    }

    @Synchronized
    private fun getNewJobs(): List<MailSendJob> {
        val jobs = newJobs.toList()
        newJobs.clear()
        return jobs
    }

    @Scheduled(cron = "0/10 * * * * ?")
    fun processNewJobs() {
        val duration = measureTimeMillis {
            val jobs = getNewJobs()
            if (jobs.isEmpty()) return
            jobs.forEach { process(it) }
        }
        logger.info("Process new jobs finished within $duration ms.")
    }

    private fun process(job: MailSendJob) {
        try {
            val result = job.process(client)
            val data = jobRepository.findByIdOrNull(job.jobId) ?: return
            if (result) {
                jobRepository.update(data.updateStatus(MailJobStatus.FINISHED, timeProvider.now()))
            } else {
                jobRepository.update(data.updateStatus(MailJobStatus.FAILED, timeProvider.now()))
                if (job.getRetryCounter() <= MAX_RETRIES) {
                    retry(job)
                }
            }
        } catch (e: Exception) {
            logger.error("Error while processing job ${job.jobId}", e)
            val data = jobRepository.findByIdOrNull(job.jobId) ?: return
            jobRepository.update(data.updateStatus(MailJobStatus.FAILED, timeProvider.now()))
        }
    }

    private val failedJobs = mutableListOf<MailSendJob>()

    @Synchronized
    private fun retry(job: MailSendJob) {
        val data = jobRepository.findByIdOrNull(job.jobId) ?: return
        if (failedJobs.size >= MAX_QUEUE_SIZE) {
            jobHistoryRepository.save(
                MailJobHistoryEntryData(
                    0,
                    "Dropped retry due to queue limit of ${MAX_QUEUE_SIZE}reached",
                    timeProvider.now(),
                    job.jobId
                )
            )
            logger.error("Drop retry job ${job.jobId}")
            jobRepository.update(data.updateStatus(MailJobStatus.FAILED, timeProvider.now()))
            return
        }

        jobRepository.update(data.updateStatus(MailJobStatus.RETRY, timeProvider.now()))
        failedJobs.add(job)
    }


    @Synchronized
    private fun getFailedJobs(): List<MailSendJob> {
        val jobs = failedJobs.toList()
        failedJobs.clear()
        return jobs
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    fun processRetryJobs() {
        val duration = measureTimeMillis {
            val jobs = getFailedJobs()
            if (jobs.isEmpty()) return
            jobs.forEach { process(it) }
        }
        logger.info("Process retry jobs finished within $duration ms.")
    }

    fun getJobs(pageable: Pageable): Page<MailJob> {
        return jobRepository.findAllOrderByUpdatedDesc(pageable).map { it.convert() }
    }

    fun getFailedJobs(pageable: Pageable): Page<MailJob> {
        return jobRepository.findAllByStatus(MailJobStatus.FAILED, pageable).map { it.convert() }
    }

    fun getJobHistory(jobId: Long, pageable: Pageable): Page<MailJobHistoryEntry> {
        return jobHistoryRepository.findByJobIdOrderByTimestampDesc(jobId, pageable).map { it.convert() }
    }


}
