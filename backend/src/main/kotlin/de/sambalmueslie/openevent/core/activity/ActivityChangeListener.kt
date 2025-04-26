package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.activity.api.Activity

interface ActivityChangeListener : BusinessObjectChangeListener<Long, Activity> {

}
