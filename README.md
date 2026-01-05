# Andromeda ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

![GitHub repo size](https://img.shields.io/github/repo-size/farsroidx/andromeda)
![GitHub last commit](https://img.shields.io/github/last-commit/farsroidx/andromeda)
![JitPack](https://img.shields.io/jitpack/v/github/farsroidx/andromeda)
![Maven Central](https://img.shields.io/maven-central/v/com.github.farsroidx/andromeda-bom)
![License](https://img.shields.io/github/license/farsroidx/andromeda)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Easy%20App%20Updater-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7388)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/7e8f094fd77044b5b26bc6c157bfbbc3)](https://app.codacy.com/gh/SirLordPouya/AndroidAppUpdater/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![](https://jitpack.io/v/SirLordPouya/AndroidAppUpdater.svg)](https://jitpack.io/#SirLordPouya/AndroidAppUpdater)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)


**Andromeda** is a modern, modular Android architecture toolkit designed to help you build scalable, maintainable, and production-ready Android applications.

This repository serves as the **parent project** for the Andromeda ecosystem and contains multiple independent modules that can be used together or individually.

---

## ✨ Goals

- Provide a **clean and modular architecture** for Android projects
- Encourage **clear separation of concerns**
- Offer a **unified BOM** for safe and consistent dependency management
- Stay **Kotlin-first** and aligned with modern Android development practices

---

## 📦 Modules

Andromeda is composed of multiple standalone modules, including but not limited to:

| Feature | Unavailable | In Development | Stable | Latest Version |
|:--------|:-----------:|:--------------:|:------:|:--------------:|
| andromeda-bom                                  |—|—|—| [![](https://jitpack.io/v/farsroidx/andromeda-bom.svg)](https://jitpack.io/#farsroidx/andromeda-bom) |
| [andromeda-foundation](lib-foundation)         | |✔|✔| [![](https://jitpack.io/v/farsroidx/andromeda-foundation.svg)](https://jitpack.io/#farsroidx/andromeda-foundation) |
| [andromeda-foundation-ktx](lib-foundation-ktx) | |✔|✔| [![](https://jitpack.io/v/farsroidx/andromeda-foundation-ktx.svg)](https://jitpack.io/#farsroidx/andromeda-foundation-ktx) |
| [andromeda-koin](lib-koin)                     |✔| | | [![](https://jitpack.io/v/farsroidx/andromeda-koin.svg)](https://jitpack.io/#farsroidx/andromeda-koin) |
| [andromeda-ktx](lib-ktx)                       | |✔|✔| [![](https://jitpack.io/v/farsroidx/andromeda-ktx.svg)](https://jitpack.io/#farsroidx/andromeda-ktx) |
| [andromeda-logging](lib-logging)               | |✔|✔| [![](https://jitpack.io/v/farsroidx/andromeda-logging.svg)](https://jitpack.io/#farsroidx/andromeda-logging) |
| [andromeda-ui](lib-ui)                         | |✔|✔| [![](https://jitpack.io/v/farsroidx/andromeda-ui.svg)](https://jitpack.io/#farsroidx/andromeda-ui) |
| [andromeda-ui-ktx](lib-ui-ktx)                 | |✔|✔| [![](https://jitpack.io/v/farsroidx/andromeda-ui-ktx.svg)](https://jitpack.io/#farsroidx/andromeda-ui-ktx) |
| [andromeda-viewmodel](lib-viewmodel)           | | |✔| [![](https://jitpack.io/v/farsroidx/andromeda-viewmodel.svg)](https://jitpack.io/#farsroidx/andromeda-viewmodel) |

Each module is versioned and published as an independent artifact, while remaining fully compatible through the Andromeda BOM.

---

## 🔗 Dependency Management (BOM)

Andromeda provides a **Bill of Materials (BOM)** to ensure all modules work seamlessly together.

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

##### in `libs.versions.toml`:
> ###### LATEST_VERSION: [![](https://jitpack.io/v/farsroidx/andromeda-bom.svg)](https://jitpack.io/#farsroidx/andromeda-bom)
```toml
[versions]
andromeda-bom = "🔝LATEST_VERSION🔝"

[libraries]
andromeda-bom            = { module = "com.github.farsroidx:andromeda-bom"            , version.ref = "andromeda-bom" }
andromeda-foundation     = { module = "com.github.farsroidx:andromeda-foundation"                                     }
andromeda-foundation-ktx = { module = "com.github.farsroidx:andromeda-foundation-ktx"                                 }
andromeda-koin           = { module = "com.github.farsroidx:andromeda-koin"                                           }
andromeda-ktx            = { module = "com.github.farsroidx:andromeda-ktx"                                            }
andromeda-logging        = { module = "com.github.farsroidx:andromeda-logging"                                        }
andromeda-ui             = { module = "com.github.farsroidx:andromeda-ui"                                             }
andromeda-ui-ktx         = { module = "com.github.farsroidx:andromeda-ui-ktx"                                         }
andromeda-viewmodel      = { module = "com.github.farsroidx:andromeda-viewmodel"                                      }

[bundles]
andromeda = [
    "andromeda-foundation",
    "andromeda-foundation-ktx",
    "andromeda-koin",
    "andromeda-ktx",
    "andromeda-logging",
    "andromeda-ui",
    "andromeda-ui-ktx",
    "andromeda-viewmodel"
]
```

##### in `build.gradle.kts`:
```kotlin
dependencies {

    implementation(
        platform(
            libs.andromeda.bom
        )
    )

    implementation(libs.bundles.andromeda)
}
```

[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)