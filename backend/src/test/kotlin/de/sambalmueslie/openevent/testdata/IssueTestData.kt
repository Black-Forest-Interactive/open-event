package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.issue.IssueCrudService
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.core.issue.api.IssueChangeRequest
import io.micronaut.http.HttpRequest

object IssueTestData {

    fun createRequest(
        subject: String = "subject",
        description: String = "description",
        error: String = "error",
        url: String = "url",
    ) = IssueChangeRequest(subject, description, error, url)

    fun updateRequest(
        subject: String = "subject-update",
        description: String = "description-update",
        error: String = "error-update",
        url: String = "url-update",
    ) = IssueChangeRequest(subject, description, error, url)

    fun create(
        service: IssueCrudService,
        actor: Account,
        account: Account,
        request: IssueChangeRequest = createRequest(),
        http: HttpRequest<*> = TestHttpRequest.mock(),
    ): Issue = service.create(actor, account, request, http)

}
