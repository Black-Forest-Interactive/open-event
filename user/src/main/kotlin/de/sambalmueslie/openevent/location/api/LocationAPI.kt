package de.sambalmueslie.openevent.location.api

import de.sambalmueslie.openevent.common.crud.AuthCrudAPI

interface LocationAPI : AuthCrudAPI<Long, Location, LocationChangeRequest> {
    companion object {
        const val PERMISSION_LOCATION_READ = "location.read"
        const val PERMISSION_LOCATION_WRITE = "location.write"
    }
}
