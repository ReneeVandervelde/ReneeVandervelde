resourceBaseUrl = "../resources"
addStyle(resource("css/main-v11.css"))
meta.robots = "index, follow"
page.title = "Publications by Renee Vandervelde"

val expectFunFeed = renderRssFeed(
    title = "Expect Fun",
    description = "A series on Kotlin development, with a focus on multiplatform applications.",
    directory = "expect-fun",
    link = absoluteUrl("index.html#expect-fun"),
)

val genericallyTypedFeed = renderRssFeed(
    title = "Generically Typed",
    description = "A collection of my thoughts on various topics.",
    directory = "generically-typed",
    link = absoluteUrl("index.html#generically-typed"),
)

addSitemap(
    updated = listOfNotNull(
        LocalDate(2024, 11, 3),
        genericallyTypedFeed.latestDate,
        expectFunFeed.latestDate
    ).maxOrNull()!!,
)

header {
    breadcrumbs {
        link("Renee Vandervelde", "../index.html")
        text("Publications")
    }
    ContentHeader(
        title = "Publications",
        subtitle = markdown("""
            These are my thoughts. I think a lot about **software** and
            **engineering practices**.
        """.trimIndent()),
    ).also(::append)
}

body {
    sections {
        inline {
            h1("\uD83C\uDF88 Expect Fun")
            formattedText {
                text("A series on ")
                strong { text("Kotlin") }
                text(" development, with a focus on ")
                strong { text("multiplatform") }
                text(" applications.")
            }
            expectFunFeed.items.map { item ->
                ArticleListing(item, "expect-fun")
            }.also(::append)
            navigation {
                link("\uD83D\uDCF0", "RSS Feed", "expect-fun/rss.xml")
            }
        }
        inline {
            h1("⌨\uFE0F Generically Typed")
            formattedText {
                text("A collection of my thoughts on various topics.")
            }
            genericallyTypedFeed.items.map { item ->
                ArticleListing(item, "generically-typed")
            }.also(::append)
            navigation {
                link("\uD83D\uDCF0", "RSS Feed", "generically-typed/rss.xml")
            }
        }
    }
}
