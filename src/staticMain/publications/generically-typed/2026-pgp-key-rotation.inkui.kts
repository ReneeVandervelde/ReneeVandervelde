include("base.inkui.part.kts")

blog(
    title = "New PGP Keys for 2026 and beyond!",
    description = """
        I've decided to rotate my personal PGP keys, using a new generation
        and storage scheme. These keys will be used for software signing
        on all of my projects and can be used for personal communications.
    """.trimIndent(),
    published = LocalDate(2026, 2, 21),
    keywords = listOf("security", "pgp", "key rotation", "cryptography"),
    body = inline(
        TextElement("""
            It has been nearly 10 years since I generated my original PGP keys that
            I use for software signing. It's about time to rotate the keys.
        """.trimIndent()),
        FormattedText {
            text("I have")
            space()
            strong {
                text("no reason")
            }
            space()
            text("to suspect that my prior keys have been compromised.")
            space()
            text("However, I will be revoking them within the next day")
            space()
            text("to ensure that the new keys are used going forward.")
        },
        TextElement("""
            Unlike the prior keys, the new root key was generated on an air-gapped
            machine and protected by secure hardware before being shredded.
            This strictly limits access to the private keys, keeping them
            safer from software compromises.
        """.trimIndent()),
        TextElement("New Root Key", style = TextStyle.H2),
        TextElement("The new pgp fingerprint I will be using going forward:"),
        FormattedText {
            code(group = true) {
                text("CDDF")
                text("22CE")
                text("9A8C")
                text("9A9A")
                text("942C")
                text("A044")
                text("B47A")
                text("9AF0")
                text("C711")
                text("67A5")
            }
        },
        FormattedText {
            text("The full keyfile can be downloaded")
            space()
            link(url = resource("../2026-ReneeVandervelde.asc")) {
                text("here")
            }
            text(".")
        },
        FormattedText {
            text("""
                Subkeys are generated directly on secure hardware and will be
                rotated every 5 years. These keys will be used for commit and
                release signing, with the first subkey being:
            """.trimIndent())
            space()
            code { text("5A59-6077") }
            text(".")
        },
        FormattedText {
            text("The new key has been signed the previous key,")
            space()
            code { text("E081-F37C") }
            text(", ")
            text("so that it may be verified using existing trust.")
        },
    )
)
