package com.reneevandervelde.markdown

import com.reneevandervelde.elements.ArticleImage
import com.reneevandervelde.elements.CitationLink
import ink.ui.render.statichtml.InkUiScript
import ink.ui.render.web.elements.CodeBlock
import ink.ui.structures.GroupingStyle
import ink.ui.structures.TextStyle
import ink.ui.structures.elements.*
import org.intellij.markdown.MarkdownElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.CODE_FENCE
import org.intellij.markdown.MarkdownElementTypes.CODE_SPAN
import org.intellij.markdown.MarkdownElementTypes.EMPH
import org.intellij.markdown.MarkdownElementTypes.FULL_REFERENCE_LINK
import org.intellij.markdown.MarkdownElementTypes.IMAGE
import org.intellij.markdown.MarkdownElementTypes.INLINE_LINK
import org.intellij.markdown.MarkdownElementTypes.LINK_DEFINITION
import org.intellij.markdown.MarkdownElementTypes.LINK_DESTINATION
import org.intellij.markdown.MarkdownElementTypes.LINK_LABEL
import org.intellij.markdown.MarkdownElementTypes.LINK_TEXT
import org.intellij.markdown.MarkdownElementTypes.LIST_ITEM
import org.intellij.markdown.MarkdownElementTypes.PARAGRAPH
import org.intellij.markdown.MarkdownElementTypes.SETEXT_1
import org.intellij.markdown.MarkdownElementTypes.SETEXT_2
import org.intellij.markdown.MarkdownElementTypes.SHORT_REFERENCE_LINK
import org.intellij.markdown.MarkdownElementTypes.STRONG
import org.intellij.markdown.MarkdownElementTypes.UNORDERED_LIST
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.MarkdownTokenTypes.Companion.CODE_FENCE_CONTENT
import org.intellij.markdown.MarkdownTokenTypes.Companion.COLON
import org.intellij.markdown.MarkdownTokenTypes.Companion.DOUBLE_QUOTE
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.ESCAPED_BACKTICKS
import org.intellij.markdown.MarkdownTokenTypes.Companion.EXCLAMATION_MARK
import org.intellij.markdown.MarkdownTokenTypes.Companion.FENCE_LANG
import org.intellij.markdown.MarkdownTokenTypes.Companion.GT
import org.intellij.markdown.MarkdownTokenTypes.Companion.HARD_LINE_BREAK
import org.intellij.markdown.MarkdownTokenTypes.Companion.HORIZONTAL_RULE
import org.intellij.markdown.MarkdownTokenTypes.Companion.LBRACKET
import org.intellij.markdown.MarkdownTokenTypes.Companion.LPAREN
import org.intellij.markdown.MarkdownTokenTypes.Companion.LT
import org.intellij.markdown.MarkdownTokenTypes.Companion.RBRACKET
import org.intellij.markdown.MarkdownTokenTypes.Companion.RPAREN
import org.intellij.markdown.MarkdownTokenTypes.Companion.SETEXT_CONTENT
import org.intellij.markdown.MarkdownTokenTypes.Companion.SINGLE_QUOTE
import org.intellij.markdown.MarkdownTokenTypes.Companion.TEXT
import org.intellij.markdown.MarkdownTokenTypes.Companion.WHITE_SPACE
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import java.io.File

private val parser = MarkdownParser(CommonMarkFlavourDescriptor())

fun InkUiScript.markdownFile(
    file: String,
    debug: Boolean = false,
    includeCitations: Boolean = false,
    excludeFirstH1: Boolean = false,
): UiElement {
    val text = File(scriptFile.parentFile.absolutePath, file).readText()
    return markdown(text, debug, includeCitations, excludeFirstH1)
}

fun InkUiScript.markdown(
    content: String,
    debug: Boolean = false,
    includeCitations: Boolean = false,
    excludeFirstH1: Boolean = false,
): UiElement {
    val ast = parser.buildMarkdownTreeFromString(content)

    if (debug) printTree(content, ast)

    val firstH1 = if (excludeFirstH1) {
        ast.findChild { it.type == ATX_1 || it.type == SETEXT_1 }
    } else null

    return runCatching {
            renderNode(
                text = content,
                node = ast,
                includeCitations = includeCitations,
                filter = { it !== firstH1 }
            )
        }
        .onFailure { println("Failed to render markdown: $it"); it.printStackTrace() }
        .getOrDefault(EmptyElement)
}

fun InkUiScript.extractFirstMarkdownH1(
    file: String,
    debug: Boolean = false,
): String {
    val text = File(scriptFile.parentFile.absolutePath, file).readText()
    val ast = parser.buildMarkdownTreeFromString(text)

    return ast.findChild { it.type == ATX_1 || it.type == SETEXT_1 }?.let {
        getContent(text, it)
    } ?: throw IllegalArgumentException("No H1 found in markdown file: $file")
}

private fun renderNode(
    text: String,
    node: ASTNode,
    includeCitations: Boolean,
    filter: (ASTNode) -> Boolean = { true },
): UiElement {
    if (!filter(node)) {
        return EmptyElement
    }
    return when (node.type) {
        ATX_1, SETEXT_1 -> TextElement(getContent(text, node), style = TextStyle.H1)
        ATX_2, SETEXT_2 -> TextElement(getContent(text, node), style = TextStyle.H2)
        ATX_3 -> TextElement(getContent(text, node), style = TextStyle.H3)
        HORIZONTAL_RULE -> DividerElement
        PARAGRAPH -> {
            val images = node.children
                .filter { it.type == IMAGE }
                .mapNotNull { it.findChildOfType(INLINE_LINK) }
                .map {
                    ArticleImage(
                        image = it.findChildOfType(LINK_DESTINATION)!!.getTextInNode(text).toString(),
                        altText = it.findChildOfType(LINK_TEXT)!!.findChildOfType(TEXT)!!.getTextInNode(text).toString(),
                    )
                }
                .toTypedArray()
            val text = renderFormattedText(text, node)

            inline(
                *images,
                text
            )
        }
        CODE_FENCE -> CodeBlock(
            code = node.getCodeContent(text),
            language = node.findChildOfType(FENCE_LANG)?.getTextInNode(text)?.toString()?.let { CodeBlock.Language(it) }
        )
        UNORDERED_LIST -> TextListElement(
            node.children
                .filter { it.type == LIST_ITEM }
                .map { it.findChildOfType(PARAGRAPH) }
                .filterNotNull()
                .map { renderFormattedText(text, it) }
        )
        LINK_DEFINITION -> if (includeCitations) CitationLink(
            url = node.findChildOfType(LINK_DESTINATION)!!.getTextInNode(text).toString(),
            text = node.findChildOfType(LINK_LABEL)!!.getTextInNode(text).toString()
        ) else EmptyElement
        else -> if (node.children.isNotEmpty()) {
            node.children
                .filter(filter)
                .map { renderNode(text, it, includeCitations) }
                .fold(emptyList<UiElement>()) { acc, element ->
                    val realLast = acc.lastOrNull { it !is EmptyElement }
                    if (acc.isNotEmpty() && element is CitationLink && realLast is CitationLink) {
                        acc - realLast + CitationLink(realLast.links + element.links)
                    } else {
                        acc + element
                    }
                }
                .let { ElementList(it, groupingStyle = GroupingStyle.Inline) }
        } else {
            val rawText = node.getTextInNode(text).toString().trim()
            if (rawText.isBlank()) EmptyElement else FormattedText(paragraph = false) {
                text(rawText)
            }
        }
    }
}

private fun ASTNode.getCodeContent(text: String): String {
    val builder = StringBuilder()
    children.forEach { child ->
        when (child.type) {
            CODE_FENCE_CONTENT -> builder.append(child.getTextInNode(text))
            EOL -> if (builder.isNotBlank()) builder.append("\n")
        }
    }

    return builder.toString()
}

private fun ASTNode.findParent(): ASTNode {
    return parent?.findParent() ?: this
}

private fun ASTNode.findChild(predicate: (ASTNode) -> Boolean): ASTNode? {
    return children.find(predicate) ?: children.firstNotNullOfOrNull { it.findChild(predicate) }
}

private fun findLink(label: String, text: String, node: ASTNode): String {
    val parent = node.findParent()
    val link = parent.findChild {
        it.type == LINK_DEFINITION && it.findChild {
            it.type == LINK_LABEL && it.getTextInNode(text).toString() == "[$label]"
        } != null
    } ?: throw IllegalArgumentException("Could not find link for label: [$label]")

    return link.findChildOfType(LINK_DESTINATION)!!.getTextInNode(text).toString()
}

private fun getContent(text: String, node: ASTNode): String {
    return when (node.type) {
        TEXT -> return node.getTextInNode(text).toString()
        WHITE_SPACE -> " "
        EOL -> "\n"
        SETEXT_1, SETEXT_2 -> node.findChildOfType(SETEXT_CONTENT)!!.getTextInNode(text).toString()
        else -> node.children.map { getContent(text, it) }.joinToString("").trim()
    }
}

private fun renderFormattedText(text: String, node: ASTNode): FormattedText {
    when (node.type) {
        PARAGRAPH -> return FormattedText {
            node.children.forEach {
                buildText(text, it)
            }
        }
        else -> throw IllegalArgumentException("Unexpected node type: ${node.type}")
    }
}

private fun FormattedText.Builder.buildText(text: String, node: ASTNode) {
    when (val type = node.type) {
        MarkdownTokenTypes.EMPH, IMAGE -> return
        STRONG -> strong {
            node.children.forEach {
                buildText(text, it)
            }
        }
        EMPH -> emphasis {
            node.children.forEach {
                buildText(text, it)
            }
        }
        EOL -> text("\n")
        WHITE_SPACE -> space()
        HARD_LINE_BREAK -> br()
        SHORT_REFERENCE_LINK -> {
            val labelNode = node.findChildOfType(LINK_LABEL)!!.findChildOfType(TEXT)!!
            val labelText = labelNode.getTextInNode(text).toString()
            val url = findLink(labelText, text, node)
            link(url) {
                text(labelText)
            }
        }
        FULL_REFERENCE_LINK -> {
            val labelNode = node.findChildOfType(LINK_LABEL)!!.findChildOfType(TEXT)!!
            val labelText = labelNode.getTextInNode(text).toString()
            val linkTextNode = node.findChildOfType(LINK_TEXT)!!.findChildOfType(TEXT)
            val linkText = linkTextNode?.getTextInNode(text)?.toString().orEmpty()
            val url = findLink(labelText, text, node)
            if (labelText.toIntOrNull() != null) {
                if (linkText != "^$labelText") {
                    text(linkText)
                }
                sup {
                    link(url) {
                        text("[$labelText]")
                    }
                }
            } else {
                link(url) {
                    text(linkText)
                }
            }
        }
        INLINE_LINK -> {
            val linkText = node.findChildOfType(LINK_TEXT)!!.findChildOfType(TEXT)!!.getTextInNode(text).toString()
            val url = node.findChildOfType(LINK_DESTINATION)!!.getTextInNode(text).toString()
            link(url) {
                text(linkText)
            }
        }
        CODE_SPAN -> code {
            text(getContent(text, node))
        }
        else -> text(node.getTextInNode(text).toString())
    }
}
