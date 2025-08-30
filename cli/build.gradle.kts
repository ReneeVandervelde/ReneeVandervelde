plugins {
    application
    kotlin("jvm")
}

configurations {
    create("extractResources")
}

application {
    applicationName = "rv-cli"
    mainClass.set("com.reneevandervelde.MainKt")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(libs.kotlinx.html)
    implementation(libs.clikt)
    implementation(libs.libsodium)
    implementation(libs.mordant)
    implementation(libs.jetbrains.markdown)
    implementation(libs.kotlinx.datetime)
    implementation(libs.ink.ui.statichtml)
    implementation(libs.slf4j.shutthefuckup)
    "extractResources"(libs.ink.ui.webcommon)
}

tasks.register<Copy>("extractResources") {
    val outputDir = file("build/output/resources")

    from({
        configurations.getByName("extractResources").map { zipTree(it) }
    }) {
        include("composeResources/com.inkapplications.ui.render_web_common.generated.resources/**")
    }
    into(outputDir)
}
