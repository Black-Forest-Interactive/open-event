package de.sambalmueslie.openevent.core.audience

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.audience.api.Audience

interface AudienceChangeListener : BusinessObjectChangeListener<Long, Audience>
