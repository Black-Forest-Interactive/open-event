package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Preferences

interface PreferencesChangeListener : BusinessObjectChangeListener<Long, Preferences>
