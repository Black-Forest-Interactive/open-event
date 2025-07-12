package de.sambalmueslie.openevent.core.message

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.message.api.Message

interface MessageChangeListener : BusinessObjectChangeListener<Long, Message>
