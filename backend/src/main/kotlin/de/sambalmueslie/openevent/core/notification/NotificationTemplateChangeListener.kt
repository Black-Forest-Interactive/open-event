package de.sambalmueslie.openevent.core.notification

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate

interface NotificationTemplateChangeListener : BusinessObjectChangeListener<Long, NotificationTemplate>
