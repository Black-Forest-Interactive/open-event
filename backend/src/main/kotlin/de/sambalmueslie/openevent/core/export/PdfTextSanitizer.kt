package de.sambalmueslie.openevent.core.export

/**
 * Removes characters the embedded PDF font cannot render or the XML parser rejects:
 * astral-plane code points (emojis), zero-width and control characters; normalizes
 * exotic whitespace to plain spaces.
 */
object PdfTextSanitizer {

    // nbsp, en/em/thin spaces, line/paragraph separator, narrow nbsp, math space, ideographic space
    private val EXOTIC_WHITESPACE = Regex("[\u00A0\u2000-\u200A\u2028\u2029\u202F\u205F\u3000]")

    // C0/C1 controls (except \t \n \r), zero-width chars, word joiner, variation selectors, BOM
    private val REMOVABLE = Regex("[\u0000-\u0008\u000B\u000C\u000E-\u001F\u007F-\u009F\u200B-\u200D\u2060\uFE0E\uFE0F\uFEFF]")

    fun sanitize(text: String): String = text
        .filterNot { it.isSurrogate() }
        .replace(EXOTIC_WHITESPACE, " ")
        .replace(REMOVABLE, "")
}
