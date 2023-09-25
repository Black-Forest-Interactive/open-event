package de.sambalmueslie.openevent.core.logic.history

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.HistoryEntry

interface HistoryEntryChangeListener : BusinessObjectChangeListener<Long, HistoryEntry>
