package com.reneevandervelde.templates

import com.reneevandervelde.absoluteUrl
import com.reneevandervelde.elements.Photo
import com.reneevandervelde.evalScript
import com.reneevandervelde.htmlFileName
import com.reneevandervelde.rss.RssItem
import com.reneevandervelde.rss.rssItem
import com.reneevandervelde.singleLine
import com.reneevandervelde.sitemap.ChangeFrequency
import com.reneevandervelde.sitemap.SitemapPriority
import com.reneevandervelde.sitemap.addSitemap
import ink.ui.render.statichtml.InkUiScript
import ink.ui.structures.GroupingStyle
import ink.ui.structures.elements.BreadcrumbElement
import ink.ui.structures.layouts.ScrollingListLayout
import kotlinx.datetime.LocalDate
import kotlinx.html.meta
import java.io.File


private val filePhotos = mutableMapOf<InkUiScript, PhotoData>()

internal var InkUiScript.photo: PhotoData?
    get() = filePhotos[this]
    set(value: PhotoData?) {
        if (value == null) {
            filePhotos.remove(this)
        } else {
            filePhotos[this] = value
        }
    }

data class PhotoData(
    val full: String,
    val thumbnail: String,
    val alt: String,
    val title: String,
    val description: String,
    val published: LocalDate,
    val taken: LocalDate?,
    val pageFileName: String,
)

fun InkUiScript.renderPhotos(
    directory: String? = null,
): List<PhotoData> {
    val photoFolder = if (directory == null) {
        scriptFile.parentFile
    } else {
        scriptFile.parentFile.resolve(directory)
    }

    return photoFolder.listFiles()
        ?.filter { it != scriptFile }
        ?.filter { !it.isDirectory }
        ?.filter { it.name.endsWith(".inkui.kts") }
        ?.map { evalScript(it) }
        ?.mapNotNull { it.photo }
        ?.toList()
        ?.sortedWith(compareByDescending<PhotoData> { it.published }.thenByDescending { it.taken })
        .orEmpty()
}

fun InkUiScript.photo(
    full: String,
    thumbnail: String,
    alt: String,
    title: String,
    description: String,
    published: LocalDate,
    taken: LocalDate? = null,
) {
    meta.robots = "index, follow"
    page.title = title
    photo = PhotoData(
        full = full,
        thumbnail = thumbnail,
        alt = alt,
        title = title,
        description = description,
        published = published,
        taken = taken,
        pageFileName = scriptFile.htmlFileName,
    )
    rssItem = RssItem(
        title = title,
        description = description.singleLine(),
        published = published,
        absoluteLink = absoluteUrl(htmlFileName),
        fileName = htmlFileName,
        image = thumbnail,
    )

    addSitemap(
        updated = listOfNotNull(
            LocalDate(2024, 11, 3),
        ).maxOrNull()!!,
        changeFrequency = ChangeFrequency.Never,
        priority = SitemapPriority.High,
    )

    addPageHeader(
        BreadcrumbElement {
            link("Renee Vandervelde", "../../index.html")
            link("Photography", "../index.html")
            text(title)
        }
    )

    addHead {
      meta(name = "og:image", content = thumbnail)
    }

    addBody(
        ScrollingListLayout(
            Photo(
                title = title,
                source = full,
                alt = alt,
                description = description,
                published = published,
                taken = taken,
            ),
            groupingStyle = GroupingStyle.Inline,
        )
    )
}
