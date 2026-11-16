package de.sambalmueslie.openevent.core.export


import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode

/**
 * Converts user-provided event descriptions (legacy rich-text HTML or plain text)
 * into plain text with line breaks, escaped as well-formed XHTML for OpenHTMLtoPDF:
 * all tags are stripped, only the text content is kept.
 */
class HtmlConverter {

    fun convert(content: String): String {
        if (content.isBlank()) return ""
        val text = if (content.contains('<')) htmlToPlainText(content) else content
        return escapeXml(text).replace("\n", "<br/>")
    }

    private fun htmlToPlainText(content: String): String {
        val doc = Jsoup.parse(content)
        doc.outputSettings(Document.OutputSettings().prettyPrint(false))
        doc.select("br").forEach { it.replaceWith(TextNode("\n")) }
        doc.select("p, li, ul, ol, div, h1, h2, h3, h4, h5, h6, tr").forEach { it.appendChild(TextNode("\n")) }
        return doc.body().wholeText()
            .lines().joinToString("\n") { it.trim() }
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }

    private fun escapeXml(text: String) = text
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
}
