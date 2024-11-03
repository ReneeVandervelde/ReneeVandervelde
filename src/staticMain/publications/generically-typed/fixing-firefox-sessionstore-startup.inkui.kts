include("base.inkui.part.kts")
useCodeBlocks = true

markdownBlog(
    description = """
        Firefox doesn't seem to respect the "open previous windows" setting
        when the browser is closed for a restart. As someone who likes to
        start from a clean slate, this was frustrating me quite a bit. 
        I decided to fix it the hard way. 
    """.trimIndent(),
    published = LocalDate(2023, 9, 1),
    keywords = listOf("firefox", "linux", "session", "resotre"),
)
