package de.sambalmueslie.openevent.integration

import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import io.micronaut.security.authentication.Authentication
import java.time.ZoneOffset
import java.time.ZonedDateTime

val accessKey = "auzNN7V0aB30poSilNi15HCiE"
val signer: Signer = HMACSigner.newSHA256Signer(accessKey)

fun getToken(vararg roles: String): String {
    val realmAccessClaim = "{\"roles\": [${roles.joinToString(separator = ",") { "\"$it\"" }}]}"
    val jwt = JWT()
        .addClaim("realm_access", realmAccessClaim)
        .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
        .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusHours(2))

    return JWT.getEncoder().encode(jwt, signer)
}

fun getAuthentication(email: String, vararg realmRoles: String): Authentication {
    val roles = mutableSetOf<String>()
    val attributes = mapOf(
        Pair("realm_access", mapOf(Pair("roles", realmRoles.toList()))),
        Pair("email", email),
        Pair("sub", "external-id"),
        Pair("preferred_username", "test-username"),
        Pair("given_name", "test-first-name"),
        Pair("family_name", "test-last-name"),
    )
    return Authentication.build("test", roles, attributes)
}