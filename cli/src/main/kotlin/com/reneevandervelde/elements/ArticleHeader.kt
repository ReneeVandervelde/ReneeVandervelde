package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.datetime.LocalDate
import kotlinx.html.*

data class ArticleHeader(
    val title: String,
    val published: LocalDate,
    val updated: LocalDate? = null,
): UiElement.Static {
    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is ArticleHeader) return RenderResult.Skipped

            header {
                h1 {
                    +element.title
                }
                p {
                    +"Published on "
                    time {
                        attributes["datetime"] = "${element.published}"
                        attributes["pubdate"] = "pubdate"
                        +element.published.toString()
                    }
                    if (element.updated != null) {
                        +"; updated on "
                        time {
                            attributes["datetime"] = "${element.updated}"
                            +element.updated.toString()
                        }
                    }
                }
            }

            return RenderResult.Rendered
        }
    }
}

