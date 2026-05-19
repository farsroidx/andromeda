pluginManagement {

    repositories {
        mavenLocal()
        maven(url = "https://maven.myket.ir")
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        maven(url = "https://maven.myket.ir")
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
    }
}