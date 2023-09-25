package de.sambalmueslie.openevent.core.logic.export


import org.apache.velocity.tools.generic.EscapeTool
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HtmlConverter(
    private val escapeTool: EscapeTool
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HtmlConverter::class.java)
    }

    fun convert(content: String): String {
        val doc = Jsoup.parse(content)
        val body = doc.body()

        val result = StringBuilder()
        body.select("p").forEach { e ->
            convertParagraph(e).let { s -> result.appendLine(s) }
        }

        return result.toString()
    }

    private fun convertParagraph(element: Element): String {
        val text = escapeTool.xml(element.text())

        val children = element.children().firstOrNull()
            ?: return "<fo:block font-size=\"12pt\">$text</fo:block>"

        val tag = children.tag().normalName()
        return when (tag) {
            "strong" -> "<fo:block font-weight=\"bold\" font-size=\"12pt\">$text</fo:block>"
            "em" -> "<fo:block font-style=\"italic\" font-size=\"12pt\">$text</fo:block>"
            "u" -> "<fo:block font-size=\"12pt\">$text</fo:block>"
            "s" -> "<fo:block font-size=\"12pt\">$text</fo:block>"
            "br" -> "<fo:block></fo:block>"
            else -> "<fo:block font-size=\"12pt\">$text</fo:block>"
        }
    }


}
