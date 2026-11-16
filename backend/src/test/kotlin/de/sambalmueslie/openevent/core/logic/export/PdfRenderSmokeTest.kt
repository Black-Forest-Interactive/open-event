package de.sambalmueslie.openevent.core.logic.export

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import de.sambalmueslie.openevent.core.export.HtmlConverter
import de.sambalmueslie.openevent.core.export.PdfTextSanitizer
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class PdfRenderSmokeTest {

    @Test
    fun renderDescriptionWithLegacyHtmlAndSpecialChars() {
        val description = HtmlConverter().convert(
            "<ul><li>Färorische Fischsuppe genießen 🎉</li><li>mit Bildern in beeindruckenden Landschaft eintauchen – „live“ für 3€</li></ul>"
        )
        val html = PdfTextSanitizer.sanitize(
            """
            <html>
            <head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <style>body { font-family: 'Liberation Sans', Helvetica, Arial, sans-serif; }</style></head>
            <body><div class="description">$description</div></body>
            </html>
            """.trimIndent()
        )

        val out = ByteArrayOutputStream()
        PdfRendererBuilder()
            .useSVGDrawer(BatikSVGDrawer())
            .useFont({ javaClass.getResourceAsStream("/fonts/LiberationSans-Regular.ttf") }, "Liberation Sans", 400, FontStyle.NORMAL, true)
            .useFont({ javaClass.getResourceAsStream("/fonts/LiberationSans-Bold.ttf") }, "Liberation Sans", 700, FontStyle.NORMAL, true)
            .withHtmlContent(html, "about:blank")
            .toStream(out)
            .run()

        val bytes = out.toByteArray()
        assertTrue(bytes.size > 1000, "PDF should not be empty, was ${bytes.size} bytes")
        assertTrue(bytes.decodeToString(0, 5).startsWith("%PDF-"), "Output should be a PDF")
    }
}
