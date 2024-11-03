package com.reneevandervelde

import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.api.SourceCode

data class ScriptException(
    override val message: String,
    override val cause: Throwable?,
    val location: SourceCode.Location?,
    val severity : ScriptDiagnostic.Severity,
    val file: String
): Throwable() {
    override fun toString(): String {
        val endPosition = location?.end?.takeIf { it != location.start }

        val locationString = when {
            location != null && endPosition != null -> "$file:${location.start.line}:${location.start.col}-${endPosition.line}:${endPosition.col}"
            location != null -> "$file:${location.start.line}:${location.start.col}"
            else -> file
        }
        return when {
            cause != null -> "$locationString ScriptException: $message\nCaused by: $cause"
            else -> "$locationString ScriptException: $message"
        }
    }
}
