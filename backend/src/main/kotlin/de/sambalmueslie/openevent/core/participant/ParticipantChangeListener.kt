package de.sambalmueslie.openevent.core.participant

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.participant.api.Participant

interface ParticipantChangeListener : BusinessObjectChangeListener<Long, Participant>
