plugins {
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }
}

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        jsMain.dependencies {
            implementation(libs.libsodium)
        }
    }
}
