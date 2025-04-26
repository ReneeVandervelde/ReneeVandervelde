package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.img

data class ArticleImage(
    val image: String,
    val altText: String,
): UiElement.Static {
    object Renderer: ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is ArticleImage) return RenderResult.Skipped

            a(
                href = element.image
            ) {
                img(
                    src = element.image,
                    alt = element.altText,
                )
            }

            return RenderResult.Rendered
        }
    }
}
