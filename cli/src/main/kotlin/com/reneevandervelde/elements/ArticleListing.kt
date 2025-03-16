package com.reneevandervelde.elements

import com.reneevandervelde.rss.RssItem
import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.datetime.LocalDate
import kotlinx.html.*

data class ArticleListing(
    val title: String,
    val description: String,
    val link: String,
    val published: LocalDate,
): UiElement.Static {
    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is ArticleListing) return RenderResult.Skipped

            article("article-index") {
                header {
                    a(href = element.link) {
                        h2 {
                            +element.title
                        }
                    }
                    time {
                        attributes["datetime"] = "${element.published}"
                        attributes["pubdate"] = "pubdate"
                        +element.published.toString()
                    }
                }
            }

            return RenderResult.Rendered
        }
    }
}
fun ArticleListing(rssItem: RssItem, baseUrl: String) = ArticleListing(
    title = rssItem.title,
    description = rssItem.description,
    link = "$baseUrl/${rssItem.fileName}",
    published = rssItem.published,
)
