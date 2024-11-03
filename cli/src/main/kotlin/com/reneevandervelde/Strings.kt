package com.reneevandervelde

/**
 * Merge a potentially multiline string into a single line.
 */
fun String.singleLine() = trimIndent().replace("\n", " ").trim()
