package de.sambalmueslie.openevent.group.api

import de.sambalmueslie.openevent.common.crud.AuthCrudAPI

interface GroupAPI : AuthCrudAPI<Long, Group, GroupChangeRequest> {
    companion object {
        const val PERMISSION_GROUP_READ = "group.read"
        const val PERMISSION_GROUP_WRITE = "group.write"
    }
}
