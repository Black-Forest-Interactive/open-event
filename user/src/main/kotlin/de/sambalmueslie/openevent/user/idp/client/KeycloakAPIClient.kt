package de.sambalmueslie.openevent.user.idp.client

import de.sambalmueslie.openevent.user.idp.api.IdpAPI
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.GroupRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation

@Client("\${keycloak.base-url}")
interface KeycloakAPIClient : IdpAPI {
    @Get("/admin/realms/\${keycloak.realm}/users")
    override fun getUserRepresentations(): List<UserRepresentation>

    @Get("/admin/realms/\${keycloak.realm}/users/{userId}")
    override fun getUserRepresentation(@PathVariable userId: String): UserRepresentation?

    @Get("/admin/realms/\${keycloak.realm}/users/{userId}/groups")
    override fun getUserGroups(@PathVariable userId: String): List<GroupRepresentation>

    @Get("/admin/realms/\${keycloak.realm}/clients")
    override fun getClients(@QueryValue() clientId: String): List<ClientRepresentation>

    @Get("/admin/realms/\${keycloak.realm}/users/{userId}/role-mappings/clients/{client}")
    override fun getUserRoles(@PathVariable userId: String, @PathVariable client: String): List<RoleRepresentation>
}
