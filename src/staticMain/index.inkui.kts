include("base.inkui.part.kts")
addStyle(resource("css/index-v1.css"))
addSitemap(
    updated = LocalDate(2024, 11, 3),
    priority = SitemapPriority.Low,
)

addPageHeader(
    inline(
        FullHeader,
        TextElement("Software Engineer, Photographer, and Friend of Open Source Software"),
    )
)

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

addBody(ScrollingListLayout(
    inline(
        markdown("""
            ## Software
            
            I've been making software professionally for over 15 years.

            I am primarily focused on **Kotlin Multiplatform** development.

            I'm currently building [BitKey](https://bitkey.build) at [Block](https://block.xyz) (dba. Square, Cash App)
        """.trimIndent()),
        LinkNavigation {
            link("\uD83D\uDD17 GitHub", "https://github.com/ReneeVandervelde")
            link("\uD83D\uDD17 LinkedIn", "https://www.linkedin.com/in/reneevandervelde/")
            link("\uD83D\uDCDC My Resume", "resume.html")
        },
    ),
    inline(
        TextElement("Recent Writing", style = TextStyle.H2),

        *genericallyTypedFeed.items
            .map { item -> ArticleListing(item, "publications/generically-typed") }
            .plus(
                expectFunFeed.items.map { item -> ArticleListing(item, "publications/expect-fun") }
            )
            .sortedByDescending { it.published }
            .take(10)
            .toTypedArray(),

        LinkNavigation {
            link("\uD83D\uDCF0 All Publications", "publications/index.html")
        },
    ),
    inline(
        markdown("""
            ## Photography

            I take photos as a hobby.
            I'm starting to upload them here, check it out!
        """.trimIndent()),
        LinkNavigation {
            link("\uD83D\uDCF7 Photography", "photography/index.html")
        }
    ),
    inline(
        TextElement("Contact", style = TextStyle.H2),
        TextElement("Social Media", style = TextStyle.H3),
        TextElement("I do not use any social media."),
        TextElement("E-Mail", style = TextStyle.H3),
        FormattedText {
            text("You can reach me at ")
            link(url = "mailto:renee@reneevandervelde.com") {
                text("renee@reneevandervelde.com")
            }
        },
        TextElement("Security", style = TextStyle.H3),
        TextElement("My PGP Fingerprint is:"),
        FormattedText {
            code(group = true) {
                text("F4F0")
                text("FCBA")
                text("19C3")
                text("71E2")
                text("FFD0")
                text("8CB6")
                text("2BDD")
                text("0590")
                text("E081")
                text("F37C")
            }
        },
        LinkNavigation {
            link("\uD83D\uDD11 Full PGP Key", "pgp-ReneeVandervelde.asc")
        },
    ),
    groupingStyle = GroupingStyle.Sections,
))


addPageFooter {
    p {
        span("emoji") { +"✌\uFE0F" }
        +" You are appreciated."
    }
}
