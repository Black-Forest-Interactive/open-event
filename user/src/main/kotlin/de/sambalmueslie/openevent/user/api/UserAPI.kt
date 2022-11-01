package de.sambalmueslie.openevent.user.api

import de.sambalmueslie.openevent.common.crud.AuthCrudAPI

interface UserAPI : AuthCrudAPI<Long, User, UserChangeRequest> {
    companion object {
        const val PERMISSION_USER_READ = "user.read"
        const val PERMISSION_USER_WRITE = "user.write"
    }
}
