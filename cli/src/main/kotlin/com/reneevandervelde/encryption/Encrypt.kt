package com.reneevandervelde.encryption

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.types.file
import com.ionspin.kotlin.crypto.aead.AuthenticatedEncryptionWithAssociatedData
import com.ionspin.kotlin.crypto.util.LibsodiumRandom
import com.ionspin.kotlin.crypto.util.encodeToUByteArray
import com.ionspin.kotlin.crypto.util.toHexString
import java.io.File
import kotlin.system.exitProcess

@OptIn(ExperimentalUnsignedTypes::class)
object Encrypt: CliktCommand()
{
    private val file by argument().file(mustExist = true, canBeDir = false)
    private val key by argument().convert { EncryptionKey.fromHex(it) }.validate { it.validate() }
    private val destination get() = File(file.parent, file.name.substringAfter("decrypted-"))

    override fun run()
    {
        if (!file.name.startsWith("decrypted-")) {
            echo("Input file must be prefixed with 'decrypted-'")
            exitProcess(1)
        }
        val encrypted = file.readText().replace("\\$\\$\\$-(.+?)-\\$\\$\\$".toRegex()) {
            encrypt(it.groupValues[1])
        }
        if (destination.exists()) {
            destination.delete()
        }
        destination.createNewFile()
        destination.writeText(encrypted)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun encrypt(message: String): String
    {
        val nonce = LibsodiumRandom.buf(24)
        val hex = AuthenticatedEncryptionWithAssociatedData.xChaCha20Poly1305IetfEncrypt(
            message = message.encodeToUByteArray(),
            nonce = nonce,
            key = key.data,
            associatedData = ubyteArrayOf(),
        ).toHexString()

        return "???$$$???${nonce.toHexString()}/$hex???$$$???"
    }
}
