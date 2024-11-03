package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.render.statichtml.renderer.renderWith
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.img

data class SideImage(
    val content: UiElement,
    val image: String,
    /**
     * Whether to retain the sticker in small viewports
     */
    val retain: Boolean = false,
): UiElement.Static {
    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is SideImage) return RenderResult.Skipped

            div("columned") {
                div("content") {
                    renderWith(
                        element = element.content,
                        consumer = consumer,
                        renderer = parent,
                    )
                }
                img(
                    classes = listOfNotNull("sticker", "retain".takeIf { element.retain }).joinToString(" "),
                    src = element.image,
                    alt = "",
                )
            }

            return RenderResult.Rendered
        }
    }
}
