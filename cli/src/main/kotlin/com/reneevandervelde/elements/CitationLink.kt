package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.*

data class CitationLink(
    val links: List<Link>,
): UiElement.Static {
    constructor(
        url: String,
        text: String,
    ): this(listOf(Link(url = url, label = text)))

    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is CitationLink) return RenderResult.Skipped

            footer("only-print") {
                ul {
                    element.links.forEach { link ->
                        li {
                            +"${link.label}: "
                            a(href = link.url) {
                                +link.url
                            }
                        }
                    }
                }
            }

            return RenderResult.Rendered
        }
    }

    data class Link(
        val url: String,
        val label: String,
    )
}
