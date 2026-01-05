pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }
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

include(":app")

include(":bom")
include(":lib-foundation")
include(":lib-foundation-ktx")
include(":lib-ktx")
include(":lib-logging")
include(":lib-ui")
include(":lib-ui-ktx")
include(":lib-viewmodel")