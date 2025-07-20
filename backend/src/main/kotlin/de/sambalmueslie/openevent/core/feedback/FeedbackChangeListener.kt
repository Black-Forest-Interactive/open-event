package de.sambalmueslie.openevent.core.feedback

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.feedback.api.Feedback

interface FeedbackChangeListener : BusinessObjectChangeListener<Long, Feedback>