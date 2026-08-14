package de.sambalmueslie.openevent.gateway.backoffice.metrics

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import java.time.LocalDate

@Controller("/api/backoffice/metrics")
@Tag(name = "BACKOFFICE Metrics API")
class MetricsController(private val service: MetricsGuardService) {

    @Get("daily")
    fun getDaily(auth: Authentication, @QueryValue from: LocalDate, @QueryValue to: LocalDate) = service.getDaily(auth, from, to)

    @Get("weekly")
    fun getWeekly(auth: Authentication, @QueryValue from: LocalDate, @QueryValue to: LocalDate) = service.getWeekly(auth, from, to)

}
