package de.sambalmueslie.openevent.core.logic.participant

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.Participant

interface ParticipantChangeListener : BusinessObjectChangeListener<Long, Participant>
