package de.sambalmueslie.openevent.core

import de.sambalmueslie.openevent.error.InsufficientPermissionsException
import io.micronaut.security.authentication.Authentication


fun <T> Authentication.checkPermission(vararg permissions: String, function: () -> T): T {
    val realmRoles = getRealmRoles()
    if (permissions.find { realmRoles.contains(it) } != null) return function.invoke()
    throw InsufficientPermissionsException("No permission to access resource", permissions.toList())
}

@Suppress("UNCHECKED_CAST")
fun Authentication.getRealmRoles(): Set<String> {
    val realmAccess = attributes["realm_access"] as? Map<String, Any> ?: return emptySet()
    val roles = realmAccess["roles"] as? List<String> ?: return emptySet()
    return roles.toSet()
}

fun Authentication.getEmail(): String {
    return attributes["email"] as? String ?: ""
}

fun Authentication.getUsername(): String {
    return attributes["preferred_username"] as? String ?: ""
}

fun Authentication.getExternalId(): String {
    return attributes["sub"] as? String ?: ""
}

fun Authentication.getFirstName(): String {
    return attributes["given_name"] as? String ?: ""
}

fun Authentication.getLastName(): String {
    return attributes["family_name"] as? String ?: ""
}

class AuthUtils

