package com.reneevandervelde.encryption

import com.ionspin.kotlin.crypto.util.LibsodiumRandom
import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import com.ionspin.kotlin.crypto.util.toHexString

@JvmInline
@OptIn(ExperimentalUnsignedTypes::class)
value class EncryptionKey(val data: UByteArray)
{
    val hex get() = data.toHexString()

    fun validate()
    {
        require(data.size == 32) { "Password size must be 32" }
    }

    override fun toString(): String = "Keystring"

    companion object
    {
        fun create() = EncryptionKey(LibsodiumRandom.buf(32))
        fun fromHex(hex: String) = EncryptionKey(hex.hexStringToUByteArray())
    }
}
