package com.reneevandervelde.elements

import com.reneevandervelde.rss.RssItem
import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.*

data class ArticleListing(
    val title: String,
    val description: String,
    val link: String
): UiElement.Static {
    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is ArticleListing) return RenderResult.Skipped

            article {
                a(href = element.link) {
                    h2 {
                        +element.title
                    }
                }
                p {
                    +element.description
                }
            }

            return RenderResult.Rendered
        }
    }
}
fun ArticleListing(rssItem: RssItem, baseUrl: String) = ArticleListing(
    title = rssItem.title,
    description = rssItem.description,
    link = "$baseUrl/${rssItem.fileName}"
)
