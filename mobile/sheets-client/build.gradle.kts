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
        val jvmMain by getting {
            dependencies {
                // jvmMain-only HTTP engine (oauth-sheets-4, E5): the JVM
                // loopback OAuth smoke-test tool needs a real engine to make
                // the token-exchange + Sheets API calls -- CIO is Ktor's
                // plain-JVM engine, no Android/native dependency implied.
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
            }
        }
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

// Manual entrypoint for the JVM OAuth loopback smoke-test tool
// (SheetsSmokeTest.kt, oauth-sheets-4 / D4 item 4). The Kotlin Multiplatform
// plugin does not apply the `application` plugin's conventional
// main-sourceSet wiring, so this JavaExec task builds its classpath directly
// from the `jvm` target's "main" compilation. NOT wired into `test`/`check` --
// this is the user's own follow-up step against real Google credentials, run
// by hand: `./gradlew runSheetsSmokeTest`.
tasks.register<JavaExec>("runSheetsSmokeTest") {
    group = "application"
    description = "Runs the JVM OAuth loopback smoke-test tool manually (real Google credentials required; never run by CI/tests)."
    val jvmMainCompilation = kotlin.jvm().compilations.getByName("main")
    dependsOn(jvmMainCompilation.compileTaskProvider)
    mainClass.set("com.passchef.sheets.SheetsSmokeTestKt")
    classpath = jvmMainCompilation.output.allOutputs + jvmMainCompilation.runtimeDependencyFiles
}
