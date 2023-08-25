package de.sambalmueslie.openevent.core.logic.location

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.Location

interface LocationChangeListener : BusinessObjectChangeListener<Long, Location>
