@file:Suppress("unused")

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import models.BuildInfo
import org.gradle.api.Project
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Represents the extension entry point for the Andromeda Gradle Plugin.
 *
 * This extension provides customizable build-time utilities and configurations
 * accessible from Gradle scripts, typically as:
 *
 * ```
 * andromeda {
 *
 *     // Extract name from: 'src/main/res/values/strings.xml'
 *
 *     // <string name="app_name">Application</string>
 *
 *     // Application [1.0.0]
 *
 *     renameOutput { "${it.appName} [${it.versionName}]" }
 *
 * }
 *
 * android {
 *
 *     defaultConfig {
 *         versionName = "1.0.0"
 *     }
 * }
 * ```
 *
 * The primary purpose of this class is to handle automatic post-build APK
 * renaming and metadata extraction from Android resources.
 *
 * @property project The Gradle [org.gradle.api.Project] to which this extension is attached.
 */
open class AndromedaExtension(
    private val project: Project,
) {
    /**
     * Automatically renames generated APK files based on app metadata and build configuration.
     *
     * The function can be configured to execute only for release builds,
     * and supports custom naming strategies through a callback function.
     *
     * @param onlyInRelease If true, renaming will occur only for non-debuggable (release) variants.
     * @param resourceFieldName The name of the string resource to use for the app name (default: `app_name`).
     * @param resourceFilePath The relative path to the strings.xml file containing the app name.
     * @param callback A lambda that generates the final APK name using the [models.BuildInfo] data model.
     */
    @Suppress("TooGenericExceptionCaught")
    fun renameOutput(
        onlyInRelease: Boolean = true,
        resourceFieldName: String = "app_name",
        resourceFilePath: String = "src/main/res/values/strings.xml",
        callback: (BuildInfo) -> String = { info -> "${info.appName} [${info.versionName}]" },
    ) {
        project.pluginManager.withPlugin("com.android.application") {
            // Locate the Android Gradle Plugin extension
            val androidComponents =
                project.extensions.findByType(AndroidComponentsExtension::class.java)
                    ?: error("AndroidComponentsExtension not found!")

            // Iterate through all Android build variants (e.g., debug, release)
            androidComponents.onVariants { variant ->

                if (variant !is ApplicationVariant) return@onVariants

                val isReleaseMode = variant.buildType?.lowercase() == "release"

                if (onlyInRelease && !isReleaseMode) return@onVariants

                val taskName =
                    variant.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }

                val newTaskName = "renameApkAfter$taskName"

                val flavorName = variant.flavorName ?: ""
                val buildType = variant.buildType ?: ""
                val variantName = variant.name

                val appIdProvider = variant.applicationId
                val mainOutput = variant.outputs.first()
                val versionNameProvider = mainOutput.versionName
                val versionCodeProvider = mainOutput.versionCode

                val renameTask =
                    project.tasks.register(newTaskName) { task ->

                        task.doLast {
                            val applicationId = appIdProvider.get()
                            val versionName = versionNameProvider.orNull ?: "unknown"
                            val versionCode = versionCodeProvider.orNull ?: 0

                            val buildDir =
                                project.layout.buildDirectory
                                    .get()
                                    .asFile

                            val apkDir = File(buildDir.parent, "${flavorName.let { if (it.isEmpty()) "" else "$it/" }}$buildType")

                            if (!apkDir.exists()) return@doLast

                            val apkFile =
                                apkDir.listFiles()?.firstOrNull { it.name.endsWith(".apk") }
                                    ?: return@doLast

                            val appName =
                                try {
                                    val file = project.file(resourceFilePath)
                                    if (!file.exists()) throw IllegalAccessException("File not found.")
                                    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
                                    doc.documentElement.normalize()
                                    val strings = doc.getElementsByTagName("string")
                                    (0 until strings.length)
                                        .map { strings.item(it) }
                                        .filterIsInstance<Element>()
                                        .firstOrNull { it.getAttribute("name") == resourceFieldName }
                                        ?.textContent ?: "app-$variantName"
                                } catch (e: ParserConfigurationException) {
                                    println("⚠️ Parser configuration error: ${e.message}")
                                    "app-${variant.name}"
                                } catch (e: SAXException) {
                                    println("⚠️ XML parsing error: ${e.message}")
                                    "app-${variant.name}"
                                } catch (e: IOException) {
                                    println("⚠️ IO error reading file '$resourceFilePath': ${e.message}")
                                    "app-${variant.name}"
                                } catch (e: Exception) {
                                    "app-$variantName"
                                }

                            val buildInfo =
                                BuildInfo(
                                    appId = applicationId,
                                    appName = appName,
                                    dirName = apkDir.name,
                                    flavorName = flavorName,
                                    variantName = variantName,
                                    versionName = versionName,
                                    versionCode = versionCode,
                                    buildType = buildType,
                                )

                            val newName = "${callback(buildInfo)}.apk"

                            val newFile = File(apkDir, newName)

                            apkFile.renameTo(newFile)

                            val userHome = System.getProperty("user.home")
                            val desktopDir = File(userHome, "Desktop")

                            val desktopApkFile = File(desktopDir, newFile.name)

                            if (newFile.exists()) {
                                newFile.copyTo(desktopApkFile, overwrite = true)
                            }

                            val releaseFolderLink =
                                "file:///" +
                                    newFile.parentFile.absolutePath
                                        .replace("\\", "/")
                                        .replace(" ", "%20")

                            val desktopFolderLink =
                                "file:///" +
                                    desktopApkFile.parentFile.absolutePath
                                        .replace("\\", "/")
                                        .replace(" ", "%20")

                            // Print a clearly formatted, clickable summary in the Gradle console
                            project.logger.lifecycle("----------------------------------------------------------------------")

                            project.logger.lifecycle(
                                """
                                📦 APK $BRIGHT_BLUE( ${newFile.name} )$RESET Successfully Generated & Renamed! 😎
                                """.trimIndent(),
                            )
                            project.logger.lifecycle("📁️ Release Folder: $releaseFolderLink")
                            project.logger.lifecycle("📁 Desktop Folder: $desktopFolderLink")

//                            project.logger.lifecycle("📁 Output Directory: $iBuildDir")
//                            project.logger.lifecycle("🔗 Desktop APK: $desktopApkLink")
//                            project.logger.lifecycle("🏷️ APK File Name: $iBuildDir\\${newFile.name}")
//                            project.logger.lifecycle("🎉 APK File Name: $desktopDir\\${newFile.name}")
                            project.logger.lifecycle("----------------------------------------------------------------------")
                        }
                    }

                project.tasks.whenTaskAdded { task ->
                    if (task.name == "assemble$taskName") {
                        task.finalizedBy(renameTask)
                    }
                }
            }
        }
    }
}
