resourceBaseUrl = "../resources"
addStyle(resource("css/main-v9.css"))
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

addPageHeader(
    BreadcrumbElement {
        link("Renee Vandervelde", "../index.html")
        text("Publications")
    }
)

addPageHeader(
    ContentHeader(
        title = "Publications",
        subtitle = markdown("""
            These are my thoughts. I think a lot about **software** and 
            **engineering practices**.
        """.trimIndent()),
    )
)

addBody(
    ScrollingListLayout(
        inline(
            TextElement("\uD83C\uDF88 Expect Fun", style = TextStyle.H1),
            markdown("""
                A series on **Kotlin** development, with a focus on
                **multiplatform** applications.
            """.trimIndent()),
            *expectFunFeed.items.map { item ->
                ArticleListing(item, "expect-fun")
            }.toTypedArray(),
            LinkNavigation {
                link("RSS Feed", "expect-fun/rss.xml")
            },
        ),
        inline(
            TextElement("⌨\uFE0F Generically Typed", style = TextStyle.H1),
            markdown("""
                A collection of my thoughts on various topics.
            """.trimIndent()),
            *genericallyTypedFeed.items.map { item ->
                ArticleListing(item, "generically-typed")
            }.toTypedArray(),
            LinkNavigation {
                link("RSS Feed", "generically-typed/rss.xml")
            },
        ),
        groupingStyle = GroupingStyle.Sections,
    )
)
