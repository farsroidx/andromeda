package models

/**
 * Data class to store detailed build information for APK output.
 *
 * This class is used by the `outputCraft` (or similar) plugin function to provide
 * contextual data for generating APK filenames, logging, or other custom tasks.
 *
 * @property appId The application ID (package name) of the variant (e.g., "com.example.app")
 * @property appName The app name extracted from the resources (strings.xml)
 * @property dirName The build directory name for this variant (e.g., "release", "debug")
 * @property flavorName The product flavor name (empty string if no flavor)
 * @property variantName Full variant name (combination of flavor + build type, e.g., "demoRelease")
 * @property versionName Application versionName as defined in build.gradle
 * @property versionCode Application versionCode as defined in build.gradle
 * @property buildType The [com.android.builder.model.BuildType] instance associated with this variant
 */
data class BuildInfo(
    val appId: String,
    val appName: String,
    val dirName: String,
    val flavorName: String,
    val variantName: String,
    val versionName: String,
    val versionCode: Int,
    val buildType: String,
)
