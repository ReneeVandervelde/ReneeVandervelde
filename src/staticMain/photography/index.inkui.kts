resourceBaseUrl = "../resources"
addStyle(resource("css/main-v9.css"))
addStyle(resource("css/photo-index-v1.css"))
meta.robots = "index, follow"
page.title = "Photography by Renee Vandervelde"

val photos = renderPhotos("photos")

val feed = renderRssFeed(
    title = "Photos",
    description = "Photography by Renee Vandervelde",
    directory = "photos",
    link = absoluteUrl(htmlFileName),
)

addSitemap(
    updated = listOfNotNull(
        feed.latestDate,
        LocalDate(2024, 11, 3),
    ).maxOrNull()!!,
)

addPageHeader(
    BreadcrumbElement {
        link("Renee Vandervelde", "../index.html")
        text("Photography")
    }
)

addPageHeader(
    ContentHeader(
        title = "Photography",
    ),
)


addBody(
    FixedGridLayout(
        columns = 3,
        items = photos.map { photo ->
            FixedGridLayout.GridItem(
                span = 1,
                horizontalPositioning = Positioning.Center,
                body = PhotoThumbnail(
                    source = photo.thumbnail,
                    alt = photo.alt,
                    caption = photo.title,
                    link = "photos/${photo.pageFileName}"
                )
            )
        }
    )
)
