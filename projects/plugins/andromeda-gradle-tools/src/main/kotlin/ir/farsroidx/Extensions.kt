@file:Suppress("unused")

package ir.farsroidx

import ir.farsroidx.managers.PropertyManager
import org.gradle.api.Project
import java.awt.Color.red

// Normal colors
private const val BLACK = "\u001B[30m"
internal const val RED = "\u001B[31m"
internal const val GREEN = "\u001B[32m"
internal const val YELLOW = "\u001B[33m"
internal const val BLUE = "\u001B[34m"
internal const val MAGENTA = "\u001B[35m"
internal const val CYAN = "\u001B[36m"
internal const val WHITE = "\u001B[37m"

// Bright colors
internal const val BRIGHT_BLACK = "\u001B[90m"
internal const val BRIGHT_RED = "\u001B[91m"
internal const val BRIGHT_GREEN = "\u001B[92m"
internal const val BRIGHT_YELLOW = "\u001B[93m"
internal const val BRIGHT_BLUE = "\u001B[94m"
internal const val BRIGHT_MAGENTA = "\u001B[95m"
internal const val BRIGHT_CYAN = "\u001B[96m"
internal const val BRIGHT_WHITE = "\u001B[97m"

// Reset
internal const val RESET = "\u001B[0m"

/**
 * Returns the property value for [key].
 *
 * Priority:
 * 1. local.properties at project root
 * 2. gradle.properties / project properties (findProperty)
 *
 * This extension uses an internal cache and will automatically reload the
 * local.properties when the file's lastModified timestamp changes.
 *
 * @receiver Project current Gradle project
 * @param key property name
 * @return property value or null string if not found
 */
fun Project.findLocalProperty(
    key: String,
    file: String = "local.properties",
): String? = PropertyManager.findLocalProperty(this, key, file)

/** Safe Int property reader with default. */
fun Project.findLocalIntProperty(
    key: String,
    default: Int = 0,
    file: String = "local.properties",
): Int = PropertyManager.getInt(this, key, default, file)

/** Safe Boolean property reader with common truthy/falsy values. */
fun Project.findLocalBooleanProperty(
    key: String,
    default: Boolean = false,
    file: String = "local.properties",
): Boolean = PropertyManager.getBoolean(this, key, default, file)

/** Safe Double property reader with default. */
fun Project.findLocalDoubleProperty(
    key: String,
    default: Double = 0.0,
    file: String = "local.properties",
): Double = PropertyManager.getDouble(this, key, default, file)

/** Reads a delimited list from a property (default delimiter is comma). */
fun Project.findLocalListProperty(
    key: String,
    delimiter: String = ",",
    file: String = "local.properties",
): List<String> = PropertyManager.getList(this, key, delimiter, file)

/**
 * Logs a debug message in green.
 *
 * @param message The message to log.
 */
fun dLog(message: Any) = println("$BRIGHT_GREEN[DEBUG] $message$RESET")

/**
 * Logs an informational message in blue.
 *
 * @param message The message to log.
 */
fun iLog(message: Any) = println("$BRIGHT_BLUE[INFO] $message$RESET")

/**
 * Logs a warning message in yellow.
 *
 * @param message The message to log.
 */
fun wLog(message: Any) = println("$BRIGHT_YELLOW[WARN] $message$RESET")

/**
 * Logs an error message in red.
 *
 * @param message The message to log.
 */
fun eLog(message: Any) = println("$red[ERROR] $message$RESET")

/**
 * Retrieves the existing Gradle extension of type [T] or creates a new one if it does not exist.
 *
 * This generic helper ensures that only a single instance of the specified extension type [T]
 * is associated with the project, preventing `DuplicateExtensionException` when the plugin
 * is applied multiple times (e.g., in both root and subprojects).
 *
 * If an extension with the given [name] already exists in the project's extension container,
 * that instance is returned. Otherwise, a new instance of [T] is created, registered, and returned.
 *
 * The `reified` type parameter allows type-safe access to the extension type at runtime.
 *
 * Example usage:
 * ```
 * val rootExt = project.createOrGetExtension<MyExtension>("myExtension")
 * rootExt.someProperty = "value"
 * ```
 *
 * @param name The name of the extension.
 * @receiver Project The Gradle project to attach or retrieve the extension from.
 * @return The extension instance of type [T] associated with this project.
 */
internal inline fun <reified T> Project.createOrGetExtension(name: String): T =
    (extensions.findByName(name) as? T)
        ?: extensions.create(
            name,
            T::class.java,
            this,
        )
