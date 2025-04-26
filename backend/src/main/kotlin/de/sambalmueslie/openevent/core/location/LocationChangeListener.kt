package de.sambalmueslie.openevent.core.location

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.location.api.Location

interface LocationChangeListener : BusinessObjectChangeListener<Long, Location>
