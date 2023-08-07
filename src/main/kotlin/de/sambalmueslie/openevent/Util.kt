package de.sambalmueslie.openevent


import org.slf4j.LoggerFactory

inline fun <T> Iterable<T>.forEachWithTryCatch(action: (T) -> Unit) {
    for (element in this) {
        try {
            action(element)
        } catch (e: Exception) {
            LoggerFactory.getLogger("Iterable").error("Exception occurred", e)
        }
    }
}
