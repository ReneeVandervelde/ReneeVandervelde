package com.reneevandervelde.sitemap

import com.reneevandervelde.absoluteUrl
import com.reneevandervelde.htmlFileName
import ink.ui.render.statichtml.InkUiScript
import kotlinx.datetime.LocalDate

private val fileSitemapEntry = mutableMapOf<InkUiScript, SitemapEntry?>()

internal var InkUiScript.sitemapEntry: SitemapEntry?
    get() = fileSitemapEntry[this]
    set(value) {
        if (value == null) {
            fileSitemapEntry.remove(this)
        } else {
            fileSitemapEntry[this] = value
        }
    }

data class Sitemap(
    val entries: List<SitemapEntry>,
) {
    val xml = """
        <?xml version='1.0' encoding='UTF-8'?>
        <urlset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"
                xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
${entries.sortedByDescending { it.lastModified }.joinToString("\n") { it.xml }.prependIndent("            ")}
        </urlset>
    """.trimIndent()
}

data class SitemapEntry(
    val absoluteLink: String,
    val lastModified: LocalDate,
    val changeFrequency: ChangeFrequency = ChangeFrequency.Monthly,
    val priority: SitemapPriority = SitemapPriority.Normal,
) {
    val xml = """
        <url>
            <loc>$absoluteLink</loc>
            <lastmod>$lastModified</lastmod>
            <changefreq>${changeFrequency.key}</changefreq>
            <priority>${priority.value}</priority>
        </url>
    """.trimIndent()
}

fun InkUiScript.addSitemap(
    updated: LocalDate,
    absoluteLink: String = absoluteUrl(htmlFileName),
    changeFrequency: ChangeFrequency = ChangeFrequency.Monthly,
    priority: SitemapPriority = SitemapPriority.Normal,
) {
    sitemapEntry = SitemapEntry(
        absoluteLink = absoluteLink,
        lastModified = updated,
        changeFrequency = changeFrequency,
        priority = priority,
    )
}

@JvmInline
value class ChangeFrequency(
    val key: String
) {
    companion object {
        val Always = ChangeFrequency("always")
        val Hourly = ChangeFrequency("hourly")
        val Daily = ChangeFrequency("daily")
        val Weekly = ChangeFrequency("weekly")
        val Monthly = ChangeFrequency("monthly")
        val Yearly = ChangeFrequency("yearly")
        val Never = ChangeFrequency("never")
    }
}

@JvmInline
value class SitemapPriority(
    val value: String,
) {
    companion object {
        val Highest = SitemapPriority("1.0")
        val High = SitemapPriority("0.8")
        val Normal = SitemapPriority("0.5")
        val Low = SitemapPriority("0.3")
        val Lowest = SitemapPriority("0.0")
    }
}
