plugins {
    kotlin("multiplatform") version "1.9.24"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Sheets API client (Ktor + kotlinx.serialization) lands in a later cell
                // (E4 / oauth-sheets-3+) once this scaffold is proven to build.
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
    }
}
