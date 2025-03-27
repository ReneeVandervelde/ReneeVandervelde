package com.reneevandervelde

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.reneevandervelde.rss.RssFeed
import com.reneevandervelde.rss.rssFeeds
import com.reneevandervelde.sitemap.Sitemap
import com.reneevandervelde.sitemap.sitemapEntry
import ink.ui.render.statichtml.InkUiScript
import kotlinx.coroutines.*
import java.io.File
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.system.exitProcess
import kotlin.time.measureTime

@OptIn(ExperimentalUnsignedTypes::class)
internal object Render : CliktCommand() {
    private val renderScope = CoroutineScope(Dispatchers.IO)
    private val source by argument(
        help = "The base directory containing source files to render",
    ).file(
        mustExist = true,
        canBeFile = true,
        canBeDir = true,
        mustBeReadable = true,
    )

    private val buildDir by option(
        help = "The directory to output the rendered files",
    ).file(
        mustExist = false,
        canBeFile = false,
        canBeDir = true,
        mustBeWritable = true,
    ).default(File("build"))

    private val output by option(
        help = "The directory to output the rendered files",
    ).file(
        mustExist = false,
        canBeFile = false,
        canBeDir = true,
        mustBeWritable = true,
    ).default(File("build/output"))

    private val verbose by option(
        help = "Print verbose output",
    ).flag()

    private val baseUrl by option(
        help = "Base URL used for absolute links",
    ).default("https://reneevandervelde.com")

    @OptIn(ExperimentalStdlibApi::class)
    private fun renderFile(file: File, renderOutput: File): Deferred<Result<Sitemap>> {
        if (renderOutput.isFile && file.isDirectory) {
            throw IllegalArgumentException("Cannot render folder to single file: ${renderOutput.path}")
        }
        if (!renderOutput.exists()) {
            verbose("Creating output directory: ${renderOutput.path}")
            renderOutput.mkdirs()
        }

        return if (file.isDirectory) {
            verbose("Render Dir: ${file.path}")
            renderScope.async {
                file.listFiles()
                    ?.filter { it.isDirectory || it.name.endsWith(".inkui.kts") }
                    ?.map { childFile ->
                        renderFile(
                            file = childFile,
                            renderOutput = renderOutput.takeIf { file == source } ?: File(
                                renderOutput,
                                file.name
                            )
                        )
                    }
                    ?.awaitAll()
                    .let {
                        if (it?.any { it.isFailure } == true) {
                            Result.failure(CompositeException.fromList(it.filter { it.isFailure }
                                .map { it.exceptionOrNull()!!; }))
                        } else {
                            it?.map { it.getOrThrow() }
                                ?.map { it.entries }
                                ?.flatten()
                                .orEmpty()
                                .let { Sitemap(it) }
                                .let { Result.success(it) }
                        }
                    }
            }
        } else {
            renderScope.async {
                runCatching {
//                    val renderTime = measureTime {
                        debug("Rendering: ${file.path}")
                        val evaluation = evalScript(file)
                        if (!evaluation.hidePage) {
                            renderHtml(evaluation, file, renderOutput)
                        }
                        evaluation.rssFeeds.forEach { feed ->
                            renderRss(feed, file, renderOutput)
                        }
//                    }
//                    success("Success ${file.path} in ${renderTime.inWholeSeconds}s")
                    Sitemap(evaluation.sitemapEntry?.let { listOf(it) }.orEmpty())
                }
            }
        }
    }

    private fun renderHtml(evaluation: InkUiScript, sourceFile: File, outputDir: File) {
        val outputFile = File(outputDir, sourceFile.htmlFileName)
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        outputFile.writeText(evaluation.getHtml())
        verbose("Completed: " + outputFile.relativeTo(this.output.parentFile).path)
    }

    private fun renderRss(feed: RssFeed, file: File, outputDir: File) {
        val baseFile = when {
            feed.directory != null -> file.parentFile.resolve(feed.directory)
            else -> file.parentFile
        }
        val outputFile = File(feed.directory?.let { outputDir.resolve(it) } ?: outputDir, feed.name)
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        if (!baseFile.isDirectory) throw IllegalArgumentException("RSS feed path not a directory: ${baseFile.absolutePath}")
        outputFile.writeText(feed.rss)
        verbose("Completed: " + outputFile.relativeTo(this.output.parentFile).path)
    }

    private fun renderSitemap(sitemap: Sitemap, outputDir: File) {
        val outputFile = File(outputDir, "sitemap.xml")
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        outputFile.writeText(sitemap.xml)
        verbose("Completed: " + outputFile.relativeTo(this.output.parentFile).path)
    }

    private fun verbose(message: String) {
        if (verbose) {
            echo(message)
        }
    }

    private fun debug(message: String) {
        echo(TextColors.magenta("> $message"))
    }

    private fun success(message: String) {
        echo(TextColors.green("+ $message"))
    }

    private fun printAllMessages(error: Throwable?) {
        if (error == null) return
        when {
            error is CompositeException -> error.exceptions.forEach { printAllMessages(it) }
            error is ScriptException -> if (error.severity >= ScriptDiagnostic.Severity.ERROR) echo(error)
            else -> error.printStackTrace()
        }
        printAllMessages(error.cause)
    }

    override fun run() {
        runBlocking {
            AbsoluteUrls.baseUrl = baseUrl
            AbsoluteUrls.sourceDir = if (source.isDirectory) source else source.parentFile
            val result = measureTime {
                renderFile(source, output).await()
                    .onFailure {
                        printAllMessages(it)
                        echo("")
                        echo(
                            TextStyles.bold(TextColors.red("FAILED")) + " ${it::class.simpleName}: ${it.message}",
                            err = true
                        )
                        exitProcess(11)
                    }
                    .onSuccess {
                        renderSitemap(it, output)
                    }
            }
            echo("")
            echo(TextStyles.bold(TextColors.green("SUCCESS")) + " in ${result.inWholeSeconds}s")
        }
    }
}
