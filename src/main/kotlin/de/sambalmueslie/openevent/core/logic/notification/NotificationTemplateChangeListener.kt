package de.sambalmueslie.openevent.core.logic.notification

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.NotificationTemplate

interface NotificationTemplateChangeListener : BusinessObjectChangeListener<Long, NotificationTemplate>
