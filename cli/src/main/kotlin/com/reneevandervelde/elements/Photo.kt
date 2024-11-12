package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.render.statichtml.renderer.renderer
import ink.ui.structures.elements.UiElement
import kotlinx.datetime.LocalDate
import kotlinx.html.*

data class Photo(
    val title: String,
    val source: String,
    val alt: String,
    val description: String,
    val published: LocalDate,
    val taken: LocalDate? = null,
): UiElement.Static {
    companion object {
        val Renderer: ElementRenderer = renderer<Photo> { element ->
            article("photo") {
                a(href = element.source) {
                    img(
                        src = element.source,
                        alt = element.alt,
                    )
                }
                header {
                    h1 {
                        +element.title
                    }
                    p {
                        if (element.taken != null) {
                            +"Taken "
                            time {
                                +element.taken.toString()
                            }
                            +"; Published "
                        }
                        time {
                            attributes["pubdate"] = "pubdate"
                            +element.published.toString()
                        }
                    }
                }
                p {
                    +element.description
                }
            }
        }
    }
}
