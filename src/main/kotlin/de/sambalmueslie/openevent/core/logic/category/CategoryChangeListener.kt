package de.sambalmueslie.openevent.core.logic.category

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.Category

interface CategoryChangeListener : BusinessObjectChangeListener<Long, Category>
