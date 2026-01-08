pluginManagement {

    // Local Plugins (publishing / signing)
    includeBuild("build-logic")

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Andromeda"

include(
    // ANDROID
    ":android:andromeda-foundation",
    ":android:andromeda-foundation-ktx",
    ":android:andromeda-ktx",
    ":android:andromeda-logging",
    ":android:andromeda-ui",
    ":android:andromeda-ui-ktx",
    ":android:andromeda-viewmodel",
    // PLUGINS
    ":plugins:andromeda-gradle-tools",
    // BOM
    ":bom:andromeda-bom"
)