package com.reneevandervelde.encryption

import com.github.ajalt.clikt.core.CliktCommand

object GenerateKey: CliktCommand()
{
    override fun run()
    {
        val key = EncryptionKey.create()

        println("${key.hex}")
    }
}
