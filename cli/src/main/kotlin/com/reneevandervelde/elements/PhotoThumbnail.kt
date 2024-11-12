package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.render.statichtml.renderer.renderer
import ink.ui.structures.elements.UiElement
import kotlinx.html.*

data class PhotoThumbnail(
    val source: String,
    val alt: String,
    val caption: String,
    val link: String,
): UiElement.Static {
    companion object {
        val Renderer: ElementRenderer = renderer<PhotoThumbnail> { element ->
            article("photo-thumbnail") {
                a(
                    href = element.link
                ) {
                    img(
                        src = element.source,
                        alt = element.alt,
                    )
                }
                p("caption") { +element.caption }
            }
        }
    }
}
