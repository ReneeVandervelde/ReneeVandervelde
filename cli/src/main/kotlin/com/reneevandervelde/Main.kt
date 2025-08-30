package com.reneevandervelde

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.reneevandervelde.encryption.Decrypt
import com.reneevandervelde.encryption.Encrypt
import com.reneevandervelde.encryption.GenerateKey
import kotlinx.coroutines.runBlocking

object Main: NoOpCliktCommand()
{
    init {
        subcommands(Render, Encrypt, Decrypt, GenerateKey)
    }
}

fun main(args: Array<String>) {
    runBlocking {
        LibsodiumInitializer.initialize()
    }
    Main.main(args)
}
