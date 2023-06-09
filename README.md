# Andromeda ![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Upwork](https://img.shields.io/badge/UpWork-6FDA44?style=for-the-badge&logo=Upwork&logoColor=white)

A library for using pre-built codes

> Library Size: ~500kb

### Installation:

#### 1. Paste `maven { url 'https://jitpack.io' }` in `settings.gradle`:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
        google()
        mavenCentral()
------> maven { url 'https://jitpack.io' }
    }
}
```

#### 2. Copy the following line in section `dependencies` in file `build.gradle` of module `app` and replace it with `LATEST_VERSION` according to the latest version in the [![](https://jitpack.io/v/farsroidx/andromeda.svg)](https://jitpack.io/#farsroidx/andromeda) repository:

```groovy
implementation 'com.github.farsroidx:Andromeda:LATEST_VERSION'
```
[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)](https://github.com/farsroidx)

### Change List

#### `v1.0.0` :
- **Added**:
    - App
    - Device
    - Dispatcher
    - Memory
    - Preference
    - Utils
        - Clipboard

#### `v1.1.0` :
- **Fixed**: `~/git.bat`.
- **Deleted**: `suspend` of the memory unit methods. Now you have access to the methods without `CoroutineScope`.

#### `v1.2.0` :
- **Changes**: There are many changes [`Recommended`].