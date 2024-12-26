package de.sambalmueslie.openevent.core.category

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.category.api.Category

interface CategoryChangeListener : BusinessObjectChangeListener<Long, Category>
