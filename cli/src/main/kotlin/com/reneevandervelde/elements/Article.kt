package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.render.statichtml.renderer.renderWith
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.*

data class Article(
    val header: ArticleHeader,
    val content: UiElement,
): UiElement.Static {
    object Renderer : ElementRenderer {
        override fun TagConsumer<*>.render(element: UiElement, parent: ElementRenderer): RenderResult {
            if (element !is Article) return RenderResult.Skipped

            article {
                renderWith(
                    element = element.header,
                    consumer = consumer,
                    renderer = parent,
                )
                renderWith(
                    element = element.content,
                    consumer = consumer,
                    renderer = parent,
                )
            }

            return RenderResult.Rendered
        }
    }
}
