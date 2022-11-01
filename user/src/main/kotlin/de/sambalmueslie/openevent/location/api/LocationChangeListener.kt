package de.sambalmueslie.openevent.location.api

import de.sambalmueslie.openevent.common.crud.BusinessObjectChangeListener

interface LocationChangeListener : BusinessObjectChangeListener<Long, Location>
