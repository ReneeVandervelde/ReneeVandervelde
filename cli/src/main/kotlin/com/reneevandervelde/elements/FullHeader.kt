package com.reneevandervelde.elements

import ink.ui.render.statichtml.renderer.renderer
import ink.ui.structures.elements.UiElement
import kotlinx.html.h1
import kotlinx.html.pre

private val ASCII = """
   ___                     _   __             __                 __   __   
  / _ \___ ___  ___ ___   | | / /__ ____  ___/ /__ _____  _____ / /__/ /__ 
 / , _/ -_) _ \/ -_) -_)  | |/ / _ `/ _ \/ _  / -_) __/ |/ / -_) / _  / -_)
/_/|_|\__/_//_/\__/\__/   |___/\_,_/_//_/\_,_/\__/_/  |___/\__/_/\_,_/\__/ 
"""

data object FullHeader: UiElement.Static {
    val Renderer = renderer<FullHeader> {
        h1("logo") {
            attributes["aria-label"] = "Renee Vandervelde"

            pre(classes = "ascii") {
                attributes["aria-hidden"] = "true"
                +ASCII
            }
        }
    }
}
