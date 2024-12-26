package de.sambalmueslie.openevent.core.notification

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.notification.api.NotificationScheme

interface NotificationSchemeChangeListener : BusinessObjectChangeListener<Long, NotificationScheme>
