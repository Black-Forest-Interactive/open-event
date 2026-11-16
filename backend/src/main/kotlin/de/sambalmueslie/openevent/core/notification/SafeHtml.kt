package de.sambalmueslie.openevent.core.notification

/**
 * Marks a value as already safe html, so the [TemplateRenderer] does not escape it again.
 * Everything else that is inserted into a html template gets escaped.
 */
data class SafeHtml(val value: String) {
    override fun toString() = value
}
