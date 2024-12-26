package de.sambalmueslie.openevent.core.history

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.history.api.HistoryEntry

interface HistoryEntryChangeListener : BusinessObjectChangeListener<Long, HistoryEntry>
