package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest

interface LocationAPI : CrudAPI<Long, Location, LocationChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.location.read"
        const val PERMISSION_WRITE = "openevent.location.write"
    }

}
