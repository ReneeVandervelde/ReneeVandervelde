package com.reneevandervelde

import com.reneevandervelde.elements.*
import ink.ui.render.statichtml.InkUiScript
import java.io.File
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.valueOr

internal val customRenderers = arrayOf(
    Article.Renderer,
    ArticleHeader.Renderer,
    ArticleListing.Renderer,
    CitationLink.Renderer,
    ContentHeader.Renderer,
    FullHeader.Renderer,
    SideImage.Renderer,
)
internal val customImports = arrayOf(
    "com.reneevandervelde.elements.*",
    "com.reneevandervelde.templates.*",
    "com.reneevandervelde.markdown.markdown",
    "com.reneevandervelde.markdown.markdownFile",
    "com.reneevandervelde.markdown.markdownFile",
    "com.reneevandervelde.markdown.extractFirstMarkdownH1",
    "com.reneevandervelde.rss.*",
    "com.reneevandervelde.sitemap.*",
    "com.reneevandervelde.absoluteUrl",
    "com.reneevandervelde.breadcrumbs",
    "kotlinx.datetime.LocalDate",
)

internal fun evalScript(file: File): InkUiScript {
    return InkUiScript.evalFile(
        scriptFile = file,
        customRenderers = customRenderers,
        customImports = customImports,
    )
        .valueOr {
            throw it.reports
                .filter { it.severity == ScriptDiagnostic.Severity.ERROR }
                .map { ScriptException(it.message, it.exception, it.location, it.severity, file.path) }
                .let { CompositeException.fromList(it) }
        }
        .returnValue
        .also {
            if (it is ResultValue.Error) {
                // TODO: Convert to script exception?
                throw it.error
            }
        }
        .scriptInstance
        .let { it as InkUiScript }
}
