include("base.inkui.part.kts")
addStyle(resource("css/resume-v1.css"))
meta.robots = "noindex"
page.title = "Renee Vandervelde / Resume"

addPageHeader(
    markdown("""
        Renee Vandervelde
        =================
        
        Contact: [renee@reneevandervelde.com](mailto:renee@reneevandervelde.com)

        Hello! I’m a software engineer with over 17 years of experience.\
        I am passionate about **Security** and **Kotlin Multiplatform**,
        as well as leading teams to establish processes to get real work done
        reliably.

        ----------
    """.trimIndent())
)

addBody(
    ScrollingListLayout(
        markdown("""
            Block
            -----
            **Staff Software Engineer**, 2023—present
            
            Led multiple complex engineering projects for an open-source
            hardware wallet from design to launch, and through major
            organizational growth and process change.

             - Built a data recovery system using multi-key cryptography that
            is resilient to multiple lost devices.

             - Designed and built a cryptographic based inheritance mechanism
             that requires multi-party verification to execute, while
             maintaining full user control and privacy.
        """.trimIndent()),

        markdown("""
            Stripe
            ------
            **Software Engineer**, 2020—2023
            
            Built an SDK for in-person credit card payments on a variety of
            custom hardware.

             - Expanded global credit card support into EMEA and APAC regions.
             - Created Tap-to-Pay for accepting cards directly on a phone
               with no other hardware.
             - Helped to hire and onboard new engineers on a regular basis.
        """.trimIndent()),

        markdown("""
            Target
            ------
            **Lead Software Engineer**, 2017—2020
            
            Lead engineer for a team building internal applications for the
            retail stores.

             - Created a transportation logistics application for the last-mile
               of product deliveries.
             - Built a mobile Point-of-Sale application to drive faster sales
               in stores.
             - Directly mentored team engineers and interns each year.
        """.trimIndent()),

        markdown("""
            OPI, CaringBridge
            -----------------------------
            **Android Consultant**, 2016—2017
            
            Provided engineering support for companies in the health and
            transportation sectors.
        """.trimIndent()),

        markdown("""
            Samsung SmartThings
            -------------------
            **Senior Android Engineer**, 2014—2016
            
             - Developed the flagship Android application that controls users’
            smart-home devices.
             - Worked closely with hardware teams to develop standards for IoT
               products.
        """.trimIndent()),

        markdown("""
            Nerdery
            ------
            **Developer III**, 2012—2014
            
            Created mobile, web and full-stack applications for a wide
            variety of clients.
             - Worked with multiple clients at a time to establish project
               requirements, build and maintain a variety of products.
             - Established development standards the company’s engineering
               teams.
        """.trimIndent()),

        markdown("""
            Parametric Technology
            --------------------
            **Software Engineer**, 2008—2012
            
             - Created developer tools for automated tests, deployments and
            code quality.
        """.trimIndent()),

        groupingStyle = GroupingStyle.Sections,
    )
)
