package com.reneevandervelde

import ink.ui.render.statichtml.InkUiScript

private val hiddenPages = mutableSetOf<InkUiScript>()

internal var InkUiScript.hidePage: Boolean
    get() = hiddenPages.contains(this)
    set(value) {
        if (value) {
            hiddenPages.add(this)
        } else {
            hiddenPages.remove(this)
        }
    }

fun InkUiScript.addScript(script: String)
{
    val field = this::class.java.superclass.getDeclaredField("scripts")
    field.isAccessible = true
    val scripts = field.get(this) as MutableList<String>

    scripts.add(script)
}
