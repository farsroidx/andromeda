# Andromeda Crypto ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Made with Love](https://img.shields.io/badge/Made%20with-%E2%9D%A4-red.svg?style=for-the-badge&logo=heart&logoColor=white)

farsroidx pre-built codes for faster and easier Android app development.

> 🪶 AAR Library Size: **~9KB**

### 📦 Andromeda Libraries

All **Andromeda libraries** are version-aligned and managed via a central **BOM** 🔒.  
This ensures all subprojects use compatible versions automatically.

🔗 **[View the Andromeda](../.)**  
🔗 **[Check the BOM Module](../bom)**

### Usage:

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
[![Maven Central](https://img.shields.io/maven-central/v/ir.farsroidx/andromeda-crypto.svg)](https://mvnrepository.com/artifact/ir.farsroidx/andromeda-crypto)
```toml
[versions]
andromeda-bom = "🔝LATEST_VERSION🔝"

[libraries]
andromeda-bom    = { module = "ir.farsroidx:andromeda-bom", version.ref = "andromeda-bom" }
andromeda-crypto = { module = "ir.farsroidx:andromeda-crypto"                             }
```

##### 3. in `build.gradle.kts`:
```kotlin
dependencies {

    implementation(
        platform(
            libs.andromeda.bom
        )
    )
    
    implementation(libs.andromeda.crypto)
}
```

[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)