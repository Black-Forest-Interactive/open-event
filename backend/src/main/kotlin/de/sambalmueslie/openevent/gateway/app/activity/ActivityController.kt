package de.sambalmueslie.openevent.gateway.app.activity

import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/activity")
@Tag(name = "APP Activity API")
class ActivityController(private val service: ActivityGuardService) {

    @Get("unread/amount")
    fun unreadAmount(auth: Authentication) = service.unreadAmount(auth)

    @Get("unread/info")
    fun unreadInfo(auth: Authentication) = service.unreadInfo(auth)

    @Get("recent")
    fun getRecentInfos(auth: Authentication, pageable: Pageable) = service.getRecentInfos(auth, pageable)

    @Put("read/{id}")
    fun markReadSingle(auth: Authentication, id: Long) = service.markReadSingle(auth, id)

    @Put("read")
    fun markReadAll(auth: Authentication) = service.markReadAll(auth)
}
