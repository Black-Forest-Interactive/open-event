package de.sambalmueslie.openevent.core.announcement

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.announcement.api.Announcement

interface AnnouncementChangeListener : BusinessObjectChangeListener<Long, Announcement>
