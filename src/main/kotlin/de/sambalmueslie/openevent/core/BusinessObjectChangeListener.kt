package de.sambalmueslie.openevent.core

import de.sambalmueslie.openevent.core.model.Account

interface BusinessObjectChangeListener<T, O : BusinessObject<T>> {
    fun handleCreated(actor: Account, obj: O) {
        // intentionally left empty
    }

    fun handleUpdated(actor: Account, obj: O) {
        // intentionally left empty
    }

    fun handleDeleted(actor: Account, obj: O) {
        // intentionally left empty
    }
}
