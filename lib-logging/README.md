# Andromeda-Logging ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

A library for using pre-built codes

> ![GitHub repo size](https://img.shields.io/github/repo-size/farsroidx/andromeda-logging)

### Installation:

#### 1. Add JitPack Maven:

##### in `settings.gradle`:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        .
        .
        ------> maven { url 'https://jitpack.io' }
    }
}
```

##### in `settings.gradle.kts`:
```kotlin
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        .
        .
        ------> maven(url = "https://jitpack.io")
    }
}
```

#### 2. Copy the following line in section `dependencies` in file `build.gradle` of module `app` and replace it with `LATEST_VERSION` according to the latest version in the repository:

### LATEST_VERSION: [![](https://jitpack.io/v/farsroidx/andromeda-logging.svg)](https://jitpack.io/#farsroidx/andromeda-logging)

##### in `build.gradle`:
```groovy
dependencies {
    implementation 'com.github.farsroidx:andromeda-logging:🔝LATEST_VERSION🔝'
}
```

##### in `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.farsroidx:andromeda-logging:🔝LATEST_VERSION🔝")
}
```

[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)