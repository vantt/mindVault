plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}
kotlin {
    jvmToolchain(17)
    androidLibrary { namespace = "dev.passchef.mobile.sheets"; compileSdk = 36; minSdk = 26 }
    jvm()
    sourceSets {
        commonMain {
            kotlin.srcDir("../sheets-client/src/commonMain/kotlin")
            dependencies {
                api("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        commonTest {
            kotlin.srcDir("../sheets-client/src/commonTest/kotlin")
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
            }
        }
        androidMain.dependencies { api("io.ktor:ktor-client-android:2.3.12") }
    }
}
