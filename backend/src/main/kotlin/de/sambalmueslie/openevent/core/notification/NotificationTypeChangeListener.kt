package de.sambalmueslie.openevent.core.notification

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.notification.api.NotificationType

interface NotificationTypeChangeListener : BusinessObjectChangeListener<Long, NotificationType>
