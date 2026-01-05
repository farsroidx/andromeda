@file:Suppress("unused")

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import java.util.Properties

private var localProperties: Properties? = null

fun Project.findLocalProperty(key: String, file: String = "local.properties"): String {

    if (localProperties == null) {

        localProperties = this.file("${this.rootDir}/$file").takeIf { it.exists() }
            ?.inputStream()?.use { Properties().apply { load(it) } }
    }

    return localProperties?.getProperty(key) ?: (this.findProperty(key) as? String) ?: ""
}

fun Project.isReleaseBuild(): Boolean = !this.version.toString().endsWith(suffix = "-SNAPSHOT")

fun Project.isSnapshotBuild(): Boolean = this.version.toString().endsWith(suffix = "-SNAPSHOT")

enum class Module(val str: String, val artifact: String) {

    BOM(str = "Bom", artifact = "bom"),

    FOUNDATION(str = "Foundation", artifact = "foundation"),

    FOUNDATION_KTX(str = "Foundation KTX", artifact = "foundation-ktx"),

    KTX(str = "KTX", artifact = "ktx"),

    LOGGING(str = "Logging", artifact = "logging"),

    UI(str = "Ui", artifact = "ui"),

    UI_KTX(str = "Ui KTX", artifact = "ui-ktx"),

    VIEWMODEL(str = "ViewModel", artifact = "viewmodel"),

}

fun MavenPublication.addPom(module: Module, group: String, version: String) {

    this.groupId = group
    this.version = version

    this.artifactId = "andromeda-${module.artifact}"

    this.pom {

        this.name.set("Andromeda ${module.str.trim()}")

        this.description.set("farsroidx pre-built codes for faster and easier Android app development.")

        this.url.set("https://github.com/farsroidx/andromeda")

        this.licenses {

            this.license {

                this.name.set("Apache-2.0")

                this.url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")

            }
        }

        this.developers {

            this.developer {

                this.id.set("farsroidx")

                this.name.set("Mohammad ali Riazati")

                this.email.set("farsroidx@gmail.com")

            }
        }

        this.scm {
            this.url.set("https://github.com/farsroidx/andromeda")
            this.connection.set("scm:git:git://github.com/farsroidx/andromeda.git")
            this.developerConnection.set("scm:git:ssh://github.com/farsroidx/andromeda.git")
        }
    }
}