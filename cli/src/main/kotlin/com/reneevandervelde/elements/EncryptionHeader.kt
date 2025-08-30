package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.ElementRenderer
import ink.ui.structures.elements.UiElement
import ink.ui.structures.render.RenderResult
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p

data object EncryptionHeader: UiElement.Static
{
    object Renderer: ElementRenderer
    {
        override fun TagConsumer<*>.render(
            element: UiElement,
            parent: ElementRenderer,
        ): RenderResult {
            if (element !is EncryptionHeader) return RenderResult.Skipped

            div("encryption-status") {
                h1 { +"Encrypted Content" }
                p("initial") { +"Page encrypted. Decrypting page contents... (requires javascript)" }
                p("failure") { +"Unable to decrypt the contents of this page." }
            }

            return RenderResult.Rendered
        }
    }
}
