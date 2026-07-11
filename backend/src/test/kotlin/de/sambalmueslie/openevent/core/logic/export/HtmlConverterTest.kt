package de.sambalmueslie.openevent.core.logic.export

import de.sambalmueslie.openevent.core.export.HtmlConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HtmlConverterTest {

    private val converter = HtmlConverter()

    @Test
    fun stripFormattingTags() {
        assertEquals("Normaler Text", converter.convert("<p>Normaler Text</p>"))
        assertEquals("Fetter Text", converter.convert("<p><strong>Fetter Text</strong></p>"))
        assertEquals("Kursiver Text", converter.convert("<p><em>Kursiver Text</em></p>"))
        assertEquals("Unterstrichener Text", converter.convert("<p><u>Unterstrichener Text</u></p>"))
        assertEquals("Durchgestrichener Text", converter.convert("<p><s>Durchgestrichener Text</s></p>"))
        assertEquals("", converter.convert("<p><br></p>"))
    }

    @Test
    fun stripListKeepingOneLinePerItem() {
        assertEquals(
            "Färorische Fischsuppe genießen<br/>mit Bildern in beeindruckenden Landschaft eintauchen",
            converter.convert("<ul><li>Färorische Fischsuppe genießen</li><li>mit Bildern in beeindruckenden Landschaft eintauchen</li></ul>")
        )
    }

    @Test
    fun keepParagraphAndLineBreaks() {
        assertEquals("Zeile eins<br/>Zeile zwei", converter.convert("<p>Zeile eins<br>Zeile zwei</p>"))
        assertEquals("Absatz eins<br/>Absatz zwei", converter.convert("<p>Absatz eins</p><p>Absatz zwei</p>"))
    }

    @Test
    fun stripMalformedHtml() {
        assertEquals("eins<br/>zwei", converter.convert("<ul><li>eins<li>zwei"))
        assertEquals("offen", converter.convert("<p><strong>offen"))
        assertEquals("", converter.convert("<script>alert('x')</script>"))
    }

    @Test
    fun convertPlainText() {
        assertEquals("Zeile eins<br/>Zeile zwei", converter.convert("Zeile eins\nZeile zwei"))
        assertEquals("Kaffee &amp; Kuchen", converter.convert("Kaffee & Kuchen"))
        assertEquals("", converter.convert("   "))
    }
}
