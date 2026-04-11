include("base.inkui.part.kts")
addStyle(resource("css/index-v2.css"))
addSitemap(
    updated = LocalDate(2024, 11, 3),
    priority = SitemapPriority.Low,
)

header {
    append(FullHeader)
    text("Software Engineer, gardener, probably a witch or something.")
    navigation {
        link("Writing", "publications/index.html")
        link("Photography", "photography/index.html")
        link("Resume", "resume.html")
    }
}

val genericallyTypedFeed = renderRssFeed(
    title = "Generically Typed",
    description = "A collection of my thoughts on various topics.",
    directory = "publications/generically-typed",
    link = absoluteUrl("publications/index.html#generically-typed"),
)

val expectFunFeed = renderRssFeed(
    title = "Expect Fun",
    description = "A series on Kotlin development, with a focus on multiplatform applications.",
    directory = "publications/expect-fun",
    link = absoluteUrl("publications/index.html#expect-fun"),
)

body {
    sections {
        inline {
            h2("Recent Writing")
            genericallyTypedFeed.items
                .map { item -> ArticleListing(item, "publications/generically-typed") }
                .plus(
                    expectFunFeed.items.map { item -> ArticleListing(item, "publications/expect-fun") }
                )
                .sortedByDescending { it.published }
                .take(10)
                .also(::append)
            navigation {
                link("\uD83D\uDCF0", "All Writing", "publications/index.html")
            }
        }
        inline {
            h2("Software")
            formattedText {
                text("I've been making software professionally for over 15 years.")
            }
            formattedText {
                text("I am primarily focused on ")
                strong { text("Kotlin Multiplatform") }
                text(" development.")
            }
            navigation {
                link("\uD83D\uDD17", "GitHub", "https://github.com/ReneeVandervelde")
                link("\uD83D\uDD17", "LinkedIn", "https://www.linkedin.com/in/reneevandervelde/")
                link("\uD83D\uDCDC", "My Resume", "resume.html")
            }
        }
        inline {
            h2("Photography")
            formattedText {
                text("I take photos as a hobby.")
                br()
                text("I sometimes upload them here, check it out!")
            }
            navigation {
                link("\uD83D\uDCF7", "Photography", "photography/index.html")
            }
        }
        inline {
            h2("Contact")
            h3("Social Media")
            text("I do not use any social media.")
            h3("E-Mail")
            formattedText {
                text("You can reach me at ")
                link(url = "mailto:renee@reneevandervelde.com") {
                    text("renee@reneevandervelde.com")
                }
            }
            h3("Security")
            text("My PGP Fingerprint is:")
            formattedText {
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
            }
            navigation {
                link("\uD83D\uDD11", "Full PGP Key", resource("../2026-ReneeVandervelde.asc"))
            }
        }
    }
}

footer {
    append(Footer)
}
