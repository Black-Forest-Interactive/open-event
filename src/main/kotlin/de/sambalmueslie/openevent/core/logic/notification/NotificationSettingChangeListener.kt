package de.sambalmueslie.openevent.core.logic.notification

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.NotificationSetting

interface NotificationSettingChangeListener : BusinessObjectChangeListener<Long, NotificationSetting>
