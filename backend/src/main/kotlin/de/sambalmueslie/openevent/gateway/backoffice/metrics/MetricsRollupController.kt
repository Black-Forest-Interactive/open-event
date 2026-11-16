package de.sambalmueslie.openevent.gateway.backoffice.metrics

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/metrics")
@Tag(name = "BACKOFFICE Metrics API")
class MetricsRollupController(private val service: MetricsGuardService) {

    @Post("weekly/recalculate")
    fun recalculateWeekly(auth: Authentication) = service.recalculateWeekly(auth)

}
