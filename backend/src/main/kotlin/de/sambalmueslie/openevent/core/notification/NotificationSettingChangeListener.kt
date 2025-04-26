package de.sambalmueslie.openevent.core.notification

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.notification.api.NotificationSetting

interface NotificationSettingChangeListener : BusinessObjectChangeListener<Long, NotificationSetting>
