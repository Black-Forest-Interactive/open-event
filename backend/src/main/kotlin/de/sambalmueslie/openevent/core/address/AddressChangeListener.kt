package de.sambalmueslie.openevent.core.address

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.address.api.Address

interface AddressChangeListener : BusinessObjectChangeListener<Long, Address>
