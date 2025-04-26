package de.sambalmueslie.openevent.core.registration

import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.search.common.SearchUpdateListener

fun interface RegistrationSearchListener : SearchUpdateListener<Registration>