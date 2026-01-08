@file:Suppress("unused", "UnusedVariable")

package ir.farsroidx

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.util.jar.JarFile

/**
 * AndromedaPlugin
 *
 * The core entry point of the Andromeda Gradle Plugin.
 * This class is automatically invoked when the plugin is applied
 * to a target [org.gradle.api.Project] via the Gradle build system.
 *
 * Responsibilities:
 *  - Display plugin metadata (version and release date) in colored logs.
 *  - Register or create the Andromeda extension for configuration.
 */
class AndromedaToolsPlugin : Plugin<Project> {
    /**
     * Called by Gradle when the plugin is applied to a project.
     *
     * @param project The target Gradle project where this plugin is being applied.
     */
    override fun apply(project: Project) {
        val pluginName = findPluginAttributes(key = "Andromeda-Name")
        val pluginGroup = findPluginAttributes(key = "Andromeda-Group")
        val pluginVersion = findPluginAttributes(key = "Andromeda-Version")
        val pluginReleaseDate = findPluginAttributes(key = "Andromeda-Release-Date")

        // Display a stylized and colorized log message with plugin metadata
        project.logger.lifecycle(
            """
            Andromeda Plugin $BRIGHT_YELLOW[v$pluginVersion]$RESET $BRIGHT_BLUE[$pluginReleaseDate]$RESET applied Successfully!
            """.trimIndent(),
        )

        // Register or retrieve the Andromeda extension to expose plugin configuration options
        project.createOrGetExtension<AndromedaExtension>("andromeda")
    }

    private fun findPluginAttributes(key: String): String? {
        val location =
            this::class.java.protectionDomain
                ?.codeSource
                ?.location
                ?: return null

        val locationUri = location.toURI()

        val locationFile = File(locationUri)

        val manifest = JarFile(locationFile).manifest

        return manifest.mainAttributes.getValue(key)
    }
}
