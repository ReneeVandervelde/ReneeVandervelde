package com.reneevandervelde

import ink.ui.render.statichtml.InkUiScript
import java.io.File

internal object AbsoluteUrls {
    lateinit var baseUrl: String
    lateinit var sourceDir: File
}

fun InkUiScript.absoluteUrl(
    path: String,
): String {
    val folder = scriptFile.parentFile.relativeTo(AbsoluteUrls.sourceDir).path
    if (folder.isBlank()) {
        return "${AbsoluteUrls.baseUrl}/$path"
    }
    return "${AbsoluteUrls.baseUrl}/$folder/$path"
}

val InkUiScript.htmlFileName: String get() {
    return scriptFile.htmlFileName
}
val File.htmlFileName: String get() {
    return name.substringBefore(".inkui.kts").let { "$it.html" }
}
