include("base.inkui.part.kts")
addStyle(resource("css/index-v1.css"))
addSitemap(
    updated = LocalDate(2024, 11, 3),
    priority = SitemapPriority.Low,
)

addPageHeader(
    inline(
        FullHeader,
        SideImage(
            content = markdown("""
                Howdy! I'm an Engineer at [Block] Photographer and friend of Open Source Software.

                Currently thriving in Minneapolis, MN.

                [Block]: https://block.xyz/
            """.trimIndent()),
            image = resource("images/stickers/hi.png"),
            retain = true,
        ),
    )
)

addBody(ScrollingListLayout(
    inline(
        markdown("""
            ## Software
            
            I am primarily focused on **Kotlin Multiplatform** development.
        """.trimIndent()),
        LinkNavigation {
            link("Publications", "publications/index.html")
            link("\uD83D\uDD17 GitHub", "https://github.com/ReneeVandervelde")
        },
    ),
    inline(
        markdown("""
            ## Professional Work
            
            I've been making software professionally for over 15 years.
            
            I'm currently building [BitKey](https://bitkey.build) at [Block](https://block.xyz) (aka Square)
        """.trimIndent()),
        LinkNavigation {
            link("My Resume", "resume.html")
            link("\uD83D\uDD17 LinkedIn", "https://www.linkedin.com/in/reneevandervelde/")
        },
    ),
    inline(
        markdown("""
            ## Photography

            I take photos as a hobby.
            I'm starting to upload them here, check it out!
        """.trimIndent()),
        LinkNavigation {
            link("Photography", "photography/index.html")
        }
    ),
    SideImage(
        content = inline(
            TextElement("Security", style = TextStyle.H2),
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
                link("Download Full Key", "pgp-ReneeVandervelde.asc")
            },
        ),
        image = resource("images/stickers/foilhat.png"),
    ),
    inline(
        TextElement("Social Media", style = TextStyle.H2),
        TextElement("I do not currently use social media."),
    ),
    SideImage(
        content = markdown("""
            ## Ham Radio
            I'm an amateur radio technician.
    
            I occasionally monitor Minnesota DMR Channels and Repeaters in the Northeast Minneapolis area.
    
            Primarily, I work with APRS, and am building a packet parser.
        """.trimIndent()),
        image = resource("images/stickers/radio.png"),
    ),
    inline(
        TextElement("Contact", style = TextStyle.H2),
        FormattedText {
            text("You can reach me at ")
            link(url = "mailto:renee@reneevandervelde.com") {
                text("renee@reneevandervelde.com")
            }
            text(".")
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
