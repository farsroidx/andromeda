# Andromeda ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)

![GitHub repo size](https://img.shields.io/github/repo-size/farsroidx/andromeda)
![GitHub last commit](https://img.shields.io/github/last-commit/farsroidx/andromeda)
![JitPack](https://img.shields.io/jitpack/v/github/farsroidx/andromeda)
![Maven Central](https://img.shields.io/maven-central/v/com.github.farsroidx/andromeda-bom)
![License](https://img.shields.io/github/license/farsroidx/andromeda)

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

- `andromeda-foundation`
- `andromeda-core`
- `andromeda-logger`
- `andromeda-viewmodel`

Each module is versioned and published as an independent artifact, while remaining fully compatible through the Andromeda BOM.

---

## 🔗 Dependency Management (BOM)

Andromeda provides a **Bill of Materials (BOM)** to ensure all modules work seamlessly together.

```kotlin
implementation(platform("com.github.farsroidx:andromeda-bom:<version>"))

implementation("com.github.farsroidx:andromeda-foundation")
implementation("com.github.farsroidx:andromeda-core")
implementation("com.github.farsroidx:andromeda-logger")
implementation("com.github.farsroidx:andromeda-viewmodel")
