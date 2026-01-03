# Andromeda Ui Ktx ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

farsroidx pre-built codes for faster and easier Android app development.

> ![GitHub repo size](https://img.shields.io/github/repo-size/farsroidx/andromeda-ui-ktx)

📌 Note:
By adding `andromeda-ui-ktx`, you do not need to add `andromeda-ui` separately, as all its functionality is already included.

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

> ###### LATEST_VERSION: [![](https://jitpack.io/v/farsroidx/andromeda-ui-ktx.svg)](https://jitpack.io/#farsroidx/andromeda-ui-ktx)

##### in `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.farsroidx:andromeda-ui-ktx:🔝LATEST_VERSION🔝")
}
```

[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)