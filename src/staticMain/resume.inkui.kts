include("base.inkui.part.kts")
meta.robots = "noindex"
page.title = "Renee Vandervelde / Resume"

addPageHeader(
    markdown("""
        Renee Vandervelde
        =================
        
        Contact: [renee@reneevandervelde.com](mailto:renee@reneevandervelde.com)

        Hello! I’m an engineer with 15 years of experience.\
        I am passionate about **Android** and **Kotlin Multiplatform**,
        as well as establishing improved agile processes that help teams
        deliver applications.
    """.trimIndent())
)
addBody {
    h1("content-break") {
        +"Experience"
    }
}

addBody(
    ScrollingListLayout(
        markdown("""
            Block
            -----
            **Software Engineer**, 2023—present
            
            Building a mobile hardware wallet that implements key management 
            and recovery tools with a focus on usability while maintaining
            high security. 
        """.trimIndent()),

        markdown("""
            Stripe
            ------
            **Software Engineer**, 2020—2023
            
            Built out global card-acceptance support for EMEA and APAC regions
            of the world on card readers driven by Android including Tap-to-Pay
             for taking card payments directly on an Android device. 
        """.trimIndent()),

        markdown("""
            Target
            ------
            **Lead Software Engineer**, 2017—2020
            
            Led a team of engineers to deliver mobile apps for internal
            business functions such as transportation logistics and mobile
            point of sale applications used by employees across stores to
            drive sales.
        """.trimIndent()),

        markdown("""
            ObjectPartners, CaringBridge
            -----------------------------
            **Android Consultant**, 2016—2017
            
            Provided engineering support for companies working on mobile 
            applications in the health and transportation sectors.
        """.trimIndent()),

        markdown("""
            Samsung SmartThings
            -------------------
            **Senior Android Engineer**, 2014—2016
            
            Developed a flagship Android application that controls users’
            smart-home devices. Worked to establish a well-defined deployment
            process and to create a modular architecture that could be reused 
            across various applications.
        """.trimIndent()),

        markdown("""
            Nerdery
            ------
            **Developer III**, 2012—2014
            
            Created Android, web and full-stack applications for a wide 
            variety of clients. Involved in establishing development standards
            for the company’s engineering teams.
        """.trimIndent()),

        markdown("""
            Parametric Technology
            --------------------
            **Software Engineer**, 2008—2012
            
            Developed tools for the company’s integration team working to 
            provide easier automated testing, deployments and code-quality
            measurements.
        """.trimIndent()),

        groupingStyle = GroupingStyle.Sections,
    )
)

addPageFooter {
    p {
        span("emoji") { +"✌\uFE0F" }
        +" Thank you for your consideration."
    }
}
