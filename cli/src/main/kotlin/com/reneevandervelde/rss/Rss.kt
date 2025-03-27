package com.reneevandervelde.rss

import com.reneevandervelde.evalScript
import ink.ui.render.statichtml.InkUiScript
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.html.*

private val fileRssItems = mutableMapOf<InkUiScript, RssItem>()
private val fileRssFeeds = mutableMapOf<InkUiScript, List<RssFeed>>()

internal var InkUiScript.rssFeeds: List<RssFeed>
    get() = fileRssFeeds[this].orEmpty()
    set(value) {
        fileRssFeeds[this] = value
    }

internal var InkUiScript.rssItem: RssItem?
    get() = fileRssItems[this]
    set(value) {
        if (value == null) {
            fileRssItems.remove(this)
        } else {
            fileRssItems[this] = value
        }
    }

data class RssItem(
    val title: String,
    val description: String,
    val published: LocalDate,
    val absoluteLink: String,
    val fileName: String? = null,
    val image: String? = null,
) {
    private val imageRss = if (image != null) """
        <media:content url="$image" medium="image" />
    """.trimIndent() else ""
    val rss = """
        <item>
            <title>$title</title>
            <pubDate>${published.atTime(0, 0, 0).toInstant(TimeZone.UTC)}</pubDate>
            <guid>$absoluteLink</guid>
            <link>$absoluteLink</link>
            <description>${description.replace("\n", " ").trim()}</description>
            $imageRss
        </item>
    """.trimIndent()
}

data class RssFeed(
    val title: String,
    val description: String,
    val items: List<RssItem>,
    val link: String,
    val directory: String? = null,
    val name: String = "rss.xml"
) {
    val latestDate: LocalDate? = items.maxByOrNull { it.published }?.published

    val rss = """
        <rss xmlns:media="http://search.yahoo.com/mrss/" version="2.0">
            <channel>
                <title>$title</title>
                <description>$description</description>
                <link>$link</link>
                <language>en</language>
${items.joinToString("\n") { it.rss }.prependIndent(" ".repeat(16))}
            </channel>
        </rss>
    """.trimIndent()
}

fun InkUiScript.addRssLink(
    title: String,
    location: String,
) {
    addHead {
        link(rel = "alternate", type = "application/rss+xml", href = location) {
            attributes["title"] = title
        }
    }
}

fun InkUiScript.rssItems(
    directory: String? = null,
): List<RssItem> {
    val rssFolder = if (directory == null) {
        scriptFile.parentFile
    } else {
        scriptFile.parentFile.resolve(directory)
    }

    return rssFolder.listFiles()
        ?.filter { it != scriptFile }
        ?.filter { !it.isDirectory }
        ?.filter { it.name.endsWith(".inkui.kts") }
        ?.map { evalScript(it) }
        ?.mapNotNull { it.rssItem }
        ?.toList()
        ?.sortedByDescending { it.published }
        .orEmpty()
}

fun InkUiScript.renderRssFeed(
    title: String,
    description: String,
    directory: String? = null,
    link: String,
    name: String = "rss.xml",
): RssFeed {
    val feed = RssFeed(
        title = title,
        description = description,
        items = rssItems(directory),
        link = link,
        directory = directory,
        name = name,
    )
    rssFeeds += feed
    addRssLink(
        title = title,
        location = directory?.let { "$it/" }.orEmpty() + name,
    )

    return feed
}
