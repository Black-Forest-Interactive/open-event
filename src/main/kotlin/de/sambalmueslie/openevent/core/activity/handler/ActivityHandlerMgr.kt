package de.sambalmueslie.openevent.core.activity.handler

import io.micronaut.context.annotation.Context
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent

@Context
class ActivityHandlerMgr(
    private val handler: List<ActivityHandler>
) : ApplicationEventListener<StartupEvent> {

    override fun onApplicationEvent(event: StartupEvent) {
        handler.forEach { it.setup() }
    }
}