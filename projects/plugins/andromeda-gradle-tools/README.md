# Andromeda Tools G-Plugin ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Made with Love](https://img.shields.io/badge/Made%20with-%E2%9D%A4-red.svg?style=for-the-badge&logo=heart&logoColor=white)

Andromeda Gradle Plugin by Farsroidx provides a set of pre-configured extensions, tasks, and utility functions
to streamline project setup and build automation. It automatically applies essential pre-build configurations,
adds reusable Kotlin DSL extensions, and simplifies dependency management for Android and JVM projects.
This plugin is designed to save development time by providing ready-to-use pre-build code, custom tasks,
and build conventions that can be immediately applied to any project.

> 🪶 AAR Plugin Size: **~20KB**

### Usage:

##### 1. in `settings.gradle.kts`:
```kotlin
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}
```

##### 2. in `libs.versions.toml`:
[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/ir.farsroidx.andromeda-gradle-tools)](https://plugins.gradle.org/plugin/ir.farsroidx.andromeda-gradle-tools)
```toml
[versions]
andromeda-tools = "🔝LATEST_VERSION🔝"

[plugins]
andromeda-tools = { id = "ir.farsroidx.andromeda-gradle-tools" , version.ref = "andromeda-tools" }
```

##### 3. in root `build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin)      apply false
    alias(libs.plugins.andromeda.tools)     apply false
}
```

##### 4. in app `build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.andromeda.tools)
}

val property1: String?      = findLocalProperty(key = "property1")
val property2: Int          = findLocalIntProperty(key = "property2", default = 0)
val property3: Boolean      = findLocalBooleanProperty(key = "property3", default = false)
val property4: Double       = findLocalDoubleProperty(key = "property4", default = 0.0)
val property5: List<String> = getLocalListProperty(key = "property5", delimiter = ",")

dLog("DEBUG LOG")

iLog("INFO LOG")

wLog("WARNING LOG")

eLog("ERROR LOG")

andromeda {
    
    // Extract name from: 'src/main/res/values/strings.xml'
    
    // <string name="app_name">Application</string>
    
    // Application [1.0.0]
    
    outputCraft { "${it.appName} [${it.versionName}]" }
}

android {
    
    defaultConfig {
        
        versionName = "1.0.0"
        
    }
    
    // others
}
```
