pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositories { google(); mavenCentral() }
}
rootProject.name = "passchef-mobile"
include(":app", ":shared", ":sheets-android")
project(":sheets-android").projectDir = file("mobile/sheets-android")
