package de.sambalmueslie.openevent.core.finder

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.finder.api.Finder

interface FinderChangeListener : BusinessObjectChangeListener<Long, Finder>
