package de.sambalmueslie.openevent.common.auth

import de.sambalmueslie.openevent.common.error.InsufficientPermissionsException
import io.micronaut.security.authentication.Authentication

fun <T> Authentication.checkPermission(permission: String, function: () -> T): T {
    if (!roles.contains(permission)) throw InsufficientPermissionsException("User '${name}' does not have '$permission' permission")
    return function.invoke()
}


