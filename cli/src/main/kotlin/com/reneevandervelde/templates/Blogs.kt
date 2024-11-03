package com.reneevandervelde.templates

import com.reneevandervelde.absoluteUrl
import com.reneevandervelde.elements.Article
import com.reneevandervelde.elements.ArticleHeader
import com.reneevandervelde.htmlFileName
import com.reneevandervelde.markdown.extractFirstMarkdownH1
import com.reneevandervelde.markdown.markdownFile
import com.reneevandervelde.rss.RssItem
import com.reneevandervelde.rss.rssItem
import com.reneevandervelde.singleLine
import com.reneevandervelde.sitemap.SitemapEntry
import com.reneevandervelde.sitemap.SitemapPriority
import com.reneevandervelde.sitemap.addSitemap
import com.reneevandervelde.sitemap.sitemapEntry
import ink.ui.render.statichtml.InkUiScript
import ink.ui.structures.GroupingStyle
import ink.ui.structures.elements.*
import ink.ui.structures.layouts.ScrollingListLayout
import kotlinx.datetime.LocalDate
import kotlinx.html.*
import java.io.File

fun InkUiScript.markdownBlog(
    fileName: String = markdownSidecarFile,
    description: String,
    keywords: List<String>,
    published: LocalDate,
    updated: LocalDate? = null,
) = blog(
    title = extractFirstMarkdownH1(fileName),
    description = description,
    keywords = keywords,
    body = markdownFile(fileName, includeCitations = true, excludeFirstH1 = true),
    published = published,
    updated = updated,
)

val InkUiScript.markdownSidecarFile: String get() {
    return scriptFile.name.substringBefore(".inkui.kts").let { "$it.part.md" }
}


fun InkUiScript.blog(
    title: String,
    description: String,
    keywords: List<String>,
    body: UiElement,
    published: LocalDate,
    updated: LocalDate? = null,
) {
    page.title = title
    page.contentBreak = true
    meta.keywords = keywords.joinToString()
    rssItem = RssItem(
        title = title,
        description = description.singleLine(),
        published = published,
        absoluteLink = absoluteUrl(htmlFileName),
        fileName = htmlFileName,
    )
    addSitemap(
        updated = updated ?: published,
        priority = SitemapPriority.Highest,
    )
    addHead {
        meta(name = "description", content = description.singleLine())
    }
    addBody(
        ScrollingListLayout(
            Article(
                header = ArticleHeader(
                    title = title,
                    published = published,
                    updated = updated,
                ),
                content = body,
            ),
            groupingStyle = GroupingStyle.Inline,
        )
    )
}

