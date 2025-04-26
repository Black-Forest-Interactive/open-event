package de.sambalmueslie.openevent.core.event

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.search.common.SearchUpdateListener

fun interface EventSearchListener : SearchUpdateListener<EventInfo>