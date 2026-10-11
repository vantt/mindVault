plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {
    jvmToolchain(17)
    androidLibrary {
        namespace = "dev.passchef.mobile.shared"
        compileSdk = 36
        minSdk = 26
    }
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies { implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3") }
        }
        val jvmAndroidMain by creating {
            dependsOn(commonMain)
        }
        val androidMain by getting { dependsOn(jvmAndroidMain) }
        val jvmMain by getting { dependsOn(jvmAndroidMain) }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.bouncycastle:bcprov-jdk18on:1.85")
            }
        }
    }
}
