package managers

import org.gradle.api.Project
import java.util.Properties

/**
 * Internal manager that caches loaded properties files and reloads them
 * only when the underlying file's lastModified changes.
 */
internal object PropertyManager {
    private var localProperties: Properties? = null

    /**
     * Read a property value for a project: priority:
     *  1) local.properties (rootDir/local.properties)
     *  2) gradle.properties or project properties (findProperty)
     */
    fun findLocalProperty(
        project: Project,
        key: String,
        file: String = "local.properties",
    ): String? {
        if (localProperties == null) {
            localProperties =
                project.rootDir
                    .resolve(file)
                    .takeIf { it.exists() }
                    ?.inputStream()
                    ?.use { Properties().apply { load(it) } }
        }

        return localProperties?.getProperty(key) ?: project.findProperty(key) as? String
    }

    fun getInt(
        project: Project,
        key: String,
        default: Int,
        file: String = "local.properties",
    ): Int = findLocalProperty(project, key, file)?.toIntOrNull() ?: default

    fun getBoolean(
        project: Project,
        key: String,
        default: Boolean,
        file: String = "local.properties",
    ): Boolean {
        val value = findLocalProperty(project, key, file)?.trim()?.lowercase()
        return when (value) {
            "true", "1", "yes", "on" -> true
            "false", "0", "no", "off" -> false
            else -> default
        }
    }

    fun getDouble(
        project: Project,
        key: String,
        default: Double,
        file: String = "local.properties",
    ): Double = findLocalProperty(project, key, file)?.toDoubleOrNull() ?: default

    fun getList(
        project: Project,
        key: String,
        delimiter: String,
        file: String = "local.properties",
    ): List<String> {
        val value = findLocalProperty(project, key, file)
        return if (value.isNullOrBlank()) {
            emptyList()
        } else {
            value
                .split(delimiter)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }
}
