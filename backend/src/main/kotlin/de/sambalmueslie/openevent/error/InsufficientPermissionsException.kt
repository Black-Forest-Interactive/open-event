package de.sambalmueslie.openevent.error


class InsufficientPermissionsException(message: String, val userId: String, val requiredRoles: List<String>) : RuntimeException(message)
