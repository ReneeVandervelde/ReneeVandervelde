package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.render.statichtml.renderer.renderWith
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.*

data class ContentHeader(
    val title: String,
    val subtitle: UiElement? = null,
): UiElement.Static {
    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is ContentHeader) return RenderResult.Skipped

            header {
                h1 {
                    +element.title
                }
                if (element.subtitle != null) {
                    renderWith(
                        element = element.subtitle,
                        consumer = consumer,
                        renderer = parent,
                    )
                }
            }

            return RenderResult.Rendered
        }
    }
}
