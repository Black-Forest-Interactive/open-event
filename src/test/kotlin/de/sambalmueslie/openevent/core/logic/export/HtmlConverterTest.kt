package de.sambalmueslie.openevent.core.logic.export

import org.apache.velocity.tools.generic.EscapeTool
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HtmlConverterTest {


    @Test()
    fun testConverter() {
        val escapeTool = EscapeTool()
        val converter = HtmlConverter(escapeTool)

        assertEquals("<fo:block font-size=\"12pt\">Normaler Text</fo:block>\n", converter.convert("<p>Normaler Text</p>"))
        assertEquals("<fo:block font-weight=\"bold\" font-size=\"12pt\">Fetter Text</fo:block>\n", converter.convert("<p><strong>Fetter Text</strong></p>"))
        assertEquals("<fo:block></fo:block>\n", converter.convert("<p><br></p>"))
        assertEquals("<fo:block font-style=\"italic\" font-size=\"12pt\">Kursiver Text</fo:block>\n", converter.convert("<p><em>Kursiver Text</em></p>"))
        assertEquals("<fo:block font-size=\"12pt\">Unterstrichener Text</fo:block>\n", converter.convert("<p><u>Unterstrichener Text</u></p>"))
        assertEquals("<fo:block font-size=\"12pt\">Durchgestrichener Text</fo:block>\n", converter.convert("<p><s>Durchgestrichener Text</s></p>"))
    }
}
