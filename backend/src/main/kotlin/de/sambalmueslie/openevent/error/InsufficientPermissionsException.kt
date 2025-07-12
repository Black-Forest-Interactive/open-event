package de.sambalmueslie.openevent.error


class InsufficientPermissionsException(message: String, val requiredRoles: List<String>) : RuntimeException(message)
