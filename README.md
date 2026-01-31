# Andromeda ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Made with Love](https://img.shields.io/badge/Made%20with-%E2%9D%A4-red.svg?style=for-the-badge&logo=heart&logoColor=white) 

[![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-bom.svg)](https://central.sonatype.com/search?q=g:ir.farsroidx&smo=true)
[![License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/4c0aaa44a2124bafa2666a06d7799f46)](https://app.codacy.com/gh/farsroidx/andromeda/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
![Min SDK](https://img.shields.io/badge/minSdk-23%2B-orange.svg?style=flat)
[![ktlint](https://img.shields.io/badge/code%20style-ktlint-7F52FF.svg?logo=kotlin&logoColor=white)](https://ktlint.github.io/)
![Last Commit](https://img.shields.io/github/last-commit/farsroidx/andromeda)
---
💚 🤍 ❤️

**Andromeda** is a modern, modular Android architecture toolkit designed to help you build scalable, maintainable, and production-ready Android applications.

This repository serves as the **parent project** for the Andromeda ecosystem and contains multiple independent modules that can be used together or individually.

---

### 🪶 Total Binary Size: **~150KB**
> Includes all published **AAR | JAR** artifacts (latest version, whitout signutures).

---

## ✨ Goals

- Provide a **clean and modular architecture** for Android projects
- Encourage **clear separation of concerns**
- Offer a **unified BOM** for safe and consistent dependency management
- Stay **Kotlin-first** and aligned with modern Android development practices

---

## 📦 Modules

Andromeda is composed of multiple standalone modules, including but not limited to:

| Feature | Unavailable | In Development | Stable |                                                                                 Latest Version                                                                                 |
|:--------|:-----------:|:--------------:|:------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| andromeda-bom                                  |—|—|—|            [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-bom.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-bom)            |
| [andromeda-foundation](projects/android/andromeda-foundation)         | |✔|✔|     [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-foundation.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-foundation)     |
| [andromeda-foundation-ktx](projects/android/andromeda-foundation-ktx) | |✔|✔| [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-foundation-ktx.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-foundation-ktx) |
| [andromeda-ktx](projects/android/andromeda-ktx)                       | |✔|✔|            [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-ktx.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-ktx)            |
| [andromeda-logging](projects/android/andromeda-logging)               | |✔|✔|        [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-logging.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-logging)        |
| [andromeda-ui](projects/android/andromeda-ui)                         | |✔|✔|             [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-ui.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-ui)             |
| [andromeda-ui-ktx](projects/android/andromeda-ui-ktx)                 | |✔|✔|         [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-ui-ktx.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-ui-ktx)         |
| [andromeda-viewmodel](projects/android/andromeda-viewmodel)           | | |✔|       [![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-viewmodel.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-viewmodel)       |
| [andromeda-gradle-tools](projects/plugins/andromeda-gradle-tools)     | |✔|✔|       [![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/ir.farsroidx.andromeda-gradle-tools)](https://plugins.gradle.org/plugin/ir.farsroidx.andromeda-gradle-tools) |

Each module is versioned and published as an independent artifact, while remaining fully compatible through the Andromeda BOM.

---

## 🔗 Dependency Management (BOM)

Andromeda provides a **Bill of Materials (BOM)** to ensure all modules work seamlessly together.

##### 1. in `settings.gradle.kts`:
```kotlin
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
```

##### 2. in `libs.versions.toml`:
[![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-bom.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-bom)
```toml
[versions]
andromeda-bom = "🔝LATEST_VERSION🔝"

[libraries]
andromeda-bom            = { module = "ir.farsroidx:andromeda-bom", version.ref = "andromeda-bom" }
andromeda-foundation     = { module = "ir.farsroidx:andromeda-foundation"                         }
andromeda-foundation-ktx = { module = "ir.farsroidx:andromeda-foundation-ktx"                     }
andromeda-ktx            = { module = "ir.farsroidx:andromeda-ktx"                                }
andromeda-logging        = { module = "ir.farsroidx:andromeda-logging"                            }
andromeda-ui             = { module = "ir.farsroidx:andromeda-ui"                                 }
andromeda-ui-ktx         = { module = "ir.farsroidx:andromeda-ui-ktx"                             }
andromeda-viewmodel      = { module = "ir.farsroidx:andromeda-viewmodel"                          }

[bundles]
andromeda = [
    "andromeda-foundation",
    "andromeda-foundation-ktx",
    "andromeda-ktx",
    "andromeda-logging",
    "andromeda-ui",
    "andromeda-ui-ktx",
    "andromeda-viewmodel"
]
```

##### 3. in `build.gradle.kts`:
```kotlin
dependencies {

    implementation(
        platform(
            libs.andromeda.bom
        )
    )

    // with bundle:
    implementation(libs.bundles.andromeda)

    // without bundle:
    implementation(libs.andromeda.foundation)
    implementation(libs.andromeda.foundation.ktx)
    implementation(libs.andromeda.ktx)
    implementation(libs.andromeda.logging)
    implementation(libs.andromeda.ui)
    implementation(libs.andromeda.ui.ktx)
    implementation(libs.andromeda.viewmodel)
}
```

[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)
