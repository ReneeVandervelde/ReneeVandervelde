package com.reneevandervelde

import com.github.ajalt.clikt.core.main
import com.ionspin.kotlin.crypto.LibsodiumInitializer
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        LibsodiumInitializer.initialize()
    }
    Render.main(args)
}
