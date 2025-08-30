import com.ionspin.kotlin.crypto.LibsodiumInitializer
import com.ionspin.kotlin.crypto.aead.AuthenticatedEncryptionWithAssociatedData
import com.ionspin.kotlin.crypto.util.decodeFromUByteArray
import com.ionspin.kotlin.crypto.util.hexStringToUByteArray
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeFilter
import org.w3c.dom.Text
import org.w3c.dom.asList

fun main() {
    window.addEventListener("load", {
        val fragment = window.location.hash.removePrefix("#")
        LibsodiumInitializer.initializeWithCallback {
            if (fragment.isNotEmpty()) {
                try {
                    decryptText(document.body!!, fragment)
                    document.body!!.classList.add("decrypted")
                } catch (e: Throwable) {
                    document.body!!.classList.add("encryption-failure")
                }
            } else {
                document.body!!.classList.add("encryption-failure")
            }
        }
    })
}

fun decryptText(root: Node, key: String) {
    val walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT) { NodeFilter.FILTER_ACCEPT }

    generateSequence { walker.nextNode() as? Text }
        .forEach { it.nodeValue = it.nodeValue?.replace("\\?\\?\\?\\$\\$\\$\\?\\?\\?(.+?)/(.+?)\\?\\?\\?\\$\\$\\$\\?\\?\\?".toRegex()) { matchResult ->
            val nonce = matchResult.groups[1]!!.value
            val cipherText = matchResult.groups[2]!!.value
            decrypt(cipherText, nonce, key)
        } }

    (root as? Element)?.let { el ->
        el.getElementsByTagName("*").asList()
            .forEach { e ->
                e.attributes.asList()
                    .forEach { attr -> attr.value = attr.value.replace("\\?\\?\\?\\$\\$\\$\\?\\?\\?(.+?)/(.+?)\\?\\?\\?\\$\\$\\$\\?\\?\\?".toRegex()) { matchResult ->
                        val nonce = matchResult.groups[1]!!.value
                        val cipherText = matchResult.groups[2]!!.value
                        decrypt(cipherText, nonce, key)
                    }
                }
            }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun decrypt(message: String, nonce: String, key: String): String
{
    val text = AuthenticatedEncryptionWithAssociatedData.xChaCha20Poly1305IetfDecrypt(
        ciphertextAndTag = message.trim().hexStringToUByteArray(),
        nonce = nonce.hexStringToUByteArray(),
        key = key.hexStringToUByteArray(),
        associatedData = ubyteArrayOf(),
    ).decodeFromUByteArray()

    return text
}
