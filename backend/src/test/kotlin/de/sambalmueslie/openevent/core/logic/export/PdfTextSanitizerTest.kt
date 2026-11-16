package de.sambalmueslie.openevent.core.logic.export

import de.sambalmueslie.openevent.core.export.PdfTextSanitizer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PdfTextSanitizerTest {

    @Test
    fun removeEmojis() {
        assertEquals("Sommerfest ", PdfTextSanitizer.sanitize("Sommerfest 🎉"))
        assertEquals("Grillen  am See", PdfTextSanitizer.sanitize("Grillen 🍢🔥 am See"))
    }

    @Test
    fun normalizeExoticWhitespace() {
        val narrowNbsp = 0x202F.toChar()
        val nbsp = 0x00A0.toChar()
        assertEquals("18:00 Uhr", PdfTextSanitizer.sanitize("18:00${narrowNbsp}Uhr"))
        assertEquals("Kaffee und Kuchen", PdfTextSanitizer.sanitize("Kaffee${nbsp}und${nbsp}Kuchen"))
    }

    @Test
    fun removeControlAndZeroWidthChars() {
        val zeroWidthSpace = 0x200B.toChar()
        val bell = 0x0007.toChar()
        assertEquals("Vortrag", PdfTextSanitizer.sanitize("Vor${zeroWidthSpace}trag${bell}"))
    }

    @Test
    fun keepRegularTextIncludingCp1252Specials() {
        val text = "Fest – „Grillen & Chillen“ für 3–4 €, Café Menü, süß!"
        assertEquals(text, PdfTextSanitizer.sanitize(text))
        assertEquals("Zeile 1\nZeile 2\tTab", PdfTextSanitizer.sanitize("Zeile 1\nZeile 2\tTab"))
    }
}
