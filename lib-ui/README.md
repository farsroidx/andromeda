# Andromeda Ui ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

farsroidx pre-built codes for faster and easier Android app development.

> ![GitHub repo size](https://img.shields.io/github/repo-size/farsroidx/andromeda-ui)

### Installation:

##### in `settings.gradle.kts`:
```kotlin
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://jitpack.io") <------
    }
}
```

> ###### LATEST_VERSION: [![](https://jitpack.io/v/farsroidx/andromeda-ui.svg)](https://jitpack.io/#farsroidx/andromeda-ui)

##### in `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.farsroidx:andromeda-ui:🔝LATEST_VERSION🔝")
}
```

[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)