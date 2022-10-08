package de.sambalmueslie.openevent.user.idp.api

import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.GroupRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation

interface IdpAPI {
	fun getUserRepresentation(userId: String): UserRepresentation?
	fun getUserGroups(userId: String): List<GroupRepresentation>
	fun getUserRoles(userId: String, client: String): List<RoleRepresentation>
	fun getUserRepresentations(): List<UserRepresentation>
	fun getClients(clientId: String): List<ClientRepresentation>
}
