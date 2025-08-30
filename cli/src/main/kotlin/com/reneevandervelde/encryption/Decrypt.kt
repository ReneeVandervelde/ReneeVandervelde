package com.reneevandervelde.encryption

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.types.file
import com.ionspin.kotlin.crypto.aead.AuthenticatedEncryptionWithAssociatedData
import com.ionspin.kotlin.crypto.util.decodeFromUByteArray
import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import java.io.File

object Decrypt: CliktCommand()
{
    private val file by argument().file(mustExist = true, canBeDir = false)
    private val key by argument().convert { EncryptionKey.fromHex(it) }.validate { it.validate() }
    private val destination get() = File(file.parent, "decrypted-${file.name}")

    override fun run()
    {
        val decrypted = file.readText().replace("\\?\\?\\?\\$\\$\\$\\?\\?\\?(.+?)/(.+?)\\?\\?\\?\\$\\$\\$\\?\\?\\?".toRegex()) {
            val nonce = it.groupValues[1]
            val message = it.groupValues[2]
            decrypt(message, nonce)
        }

        if (destination.exists()) {
            destination.delete()
        }
        destination.createNewFile()
        destination.writeText(decrypted)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun decrypt(message: String, nonce: String): String
    {
        val text = AuthenticatedEncryptionWithAssociatedData.xChaCha20Poly1305IetfDecrypt(
            ciphertextAndTag = message.trim().hexStringToUByteArray(),
            nonce = nonce.hexStringToUByteArray(),
            key = key.data,
            associatedData = ubyteArrayOf(),
        ).decodeFromUByteArray()

        return "\$\$\$-$text-\$\$\$"
    }
}
