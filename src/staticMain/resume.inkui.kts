include("base.inkui.part.kts")
addStyle(resource("css/resume-v1.css"))
meta.robots = "noindex"
page.title = "Renee Vandervelde / Resume"

header {
    h1("Renee Vandervelde")
    formattedText {
        text("Contact: ")
        link("mailto:renee@reneevandervelde.com") {
            text("renee@reneevandervelde.com")
        }
    }
    formattedText {
        text("Hello! I’m a software engineer with over 17 years of experience.")
        br()
        text("I am passionate about ")
        strong { text("Security") }
        text(" and ")
        strong { text("Kotlin Multiplatform") }
        text(", as well as leading teams to establish processes to get real work done reliably.")
    }
    divider()
}

body {
    sections {
        inline {
            h2("Block")
            formattedText {
                strong { text("Staff Software Engineer") }
                text(", Oct 2023 — Apr 2026")
            }
            text("""
                Led multiple complex engineering projects for an open-source
                hardware wallet from design to launch, and through major
                organizational growth and process change.
            """.trimIndent())
            textList {
                formattedText {
                    text("""
                        Built a data recovery system using multi-key
                        cryptography that is resilient to multiple lost devices.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Designed and built a cryptographic based inheritance mechanism
                        that requires multi-party verification to execute, while
                        maintaining full user control and privacy.
                    """.trimIndent())
                }
            }
        }
        inline {
            h2("Stripe")
            formattedText {
                strong { text("Software Engineer") }
                text(", Oct 2020 — Oct 2023")
            }
            text("""
                Built an SDK for in-person credit card payments on a variety of
                custom hardware.
            """.trimIndent())
            textList {
                formattedText {
                    text("""
                        Expanded global credit card support into EMEA and APAC regions.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Created Tap-to-Pay for accepting cards directly on a phone
                        with no other hardware.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Helped to hire and onboard new engineers on a regular basis.
                    """.trimIndent())
                }
            }
        }
        inline {
            h2("Target")
            formattedText {
                strong { text("Lead Software Engineer") }
                text(", Nov 2017 — Oct 2020")
            }
            text("""
                Lead engineer for a team building internal applications for the
                retail stores.
            """.trimIndent())
            textList {
                formattedText {
                    text("""
                        Created a transportation logistics application for the last-mile
                        of product deliveries.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Built a mobile Point-of-Sale application to drive faster sales
                        in stores.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Directly mentored team engineers and interns each year.
                    """.trimIndent())
                }
            }
        }
        inline {
            h2("OPI, CaringBridge")
            formattedText {
                strong { text("Android Consultant") }
                text(", Jan 2016 — Nov 2017")
            }
            text("""
                Provided engineering support for companies in the health and
                transportation sectors.
            """.trimIndent())
        }
        inline {
            h2("Samsung SmartThings")
            formattedText {
                strong { text("Senior Android Engineer") }
                text(", Sep 2014 — Jan 2016")
            }
            textList {
                formattedText {
                    text("""
                        Developed the flagship Android application that controls users’
                        smart-home devices.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Worked closely with hardware teams to develop standards for IoT
                        products.
                    """.trimIndent())
                }
            }
        }
        inline {
            h2("Nerdery")
            formattedText {
                strong { text("Developer III") }
                text(", Jun 2012 — Sep 2014")
            }
            text("""
                Created mobile, web and full-stack applications for a wide
                variety of clients.
            """.trimIndent())
            textList {
                formattedText {
                    text("""
                        Worked with multiple clients at a time to establish project
                        requirements, build and maintain a variety of products.
                    """.trimIndent())
                }
                formattedText {
                    text("""
                        Established development standards the company’s engineering
                        teams.
                    """.trimIndent())
                }
            }
        }
        inline {
            h2("Parametric Technology")
            formattedText {
                strong { text("Software Engineer") }
                text(", Jul 2008 — Jun 2012")
            }
            textList {
                formattedText {
                    text("""
                        Created developer tools for automated tests, deployments and
                        code quality.
                    """.trimIndent())
                }
            }
        }
    }
}
