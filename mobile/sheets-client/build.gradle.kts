plugins {
    kotlin("multiplatform") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
}

repositories {
    google()
    mavenCentral()
}

val ktorVersion = "2.3.12"
val kotlinxSerializationVersion = "1.6.3"
val kotlinxCoroutinesVersion = "1.8.1"

kotlin {
    jvmToolchain(21)

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Sheets API v4 client (E4 / oauth-sheets-3): Ktor client +
                // kotlinx.serialization, per approach.md Q1 -- no fitting
                // ready-made multiplatform Sheets library exists.
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                // Fake HTTP engine for SheetsClient unit tests -- no real
                // network call, no real credentials (D4 code-only item 2).
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
    }
}

// The Kotlin Multiplatform plugin only wires up "allTests" (aggregate) and
// "jvmTest" (per-target) -- there is no plain "test" task, unlike a
// single-platform Kotlin/JVM module. Alias it so `./gradlew test` (this
// cell's verify command) is actually runnable.
tasks.register("test") {
    group = "verification"
    description = "Alias for allTests -- see comment above."
    dependsOn("allTests")
}
