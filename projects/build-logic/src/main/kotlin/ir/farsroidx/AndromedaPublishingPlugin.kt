@file:Suppress("unused")

package ir.farsroidx

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.plugins.signing.SigningExtension
import java.util.Properties

class AndromedaPublishingPlugin : Plugin<Project> {

    private var localProperties: Properties? = null

    override fun apply(project: Project) = with(project) {

        // pluginManager.apply("maven-publish")
        pluginManager.apply("signing")

        afterEvaluate {

            val groupId     = this.group.toString()
            val artifactId  = this.name.toString()
            val versionName = this.version.toString()

            // ---------- Java Platform (BOM) ------------------------------------------------------
            pluginManager.withPlugin("java-platform") {

                extensions.configure<PublishingExtension> {

                    publications.register<MavenPublication>("mavenBom") {

                        from(components.findByName("javaPlatform") ?: error("Java Platform component not found"))

                        this.groupId    = groupId
                        this.artifactId = artifactId
                        this.version    = versionName

                        pomOptions(module = artifactId.moduleName())

                        logger.lifecycle("--------------------------------------")
                        logger.lifecycle(
                            """
                            groupId: $groupId
                            projectId: $artifactId
                            moduleName: ${artifactId.moduleName()}
                            versionName: $versionName
                            """.trimIndent()
                        )
                        logger.lifecycle("--------------------------------------")
                    }
                }
            }

            // ---------- Android Library ----------------------------------------------------------
            pluginManager.withPlugin("com.android.library") {

                extensions.configure<PublishingExtension> {

                    publications.register<MavenPublication>("mavenAndroid") {

                        from(components.findByName("release") ?: error("Android Library component not found"))


                        this.groupId    = groupId
                        this.artifactId = artifactId
                        this.version    = versionName

                        pomOptions(module = artifactId.moduleName())

                        logger.lifecycle("--------------------------------------")
                        logger.lifecycle(
                            """
                            groupId: $groupId
                            projectId: $artifactId
                            moduleName: ${artifactId.moduleName()}
                            versionName: $versionName
                            """.trimIndent()
                        )
                        logger.lifecycle("--------------------------------------")
                    }
                }
            }

            // ---------- Gradle Publish -----------------------------------------------------------
            pluginManager.withPlugin("com.gradle.plugin-publish") {

                extensions.configure<PublishingExtension> {

                    publications.register<MavenPublication>("mavenPlugin") {

                        from(components.findByName("java") ?: error("Java component not found"))

                        this.groupId    = groupId
                        this.artifactId = artifactId
                        this.version    = versionName

                        logger.lifecycle("--------------------------------------")
                        logger.lifecycle(
                            """
                            groupId: $groupId
                            projectId: $artifactId
                            moduleName: ${artifactId.moduleName()}
                            versionName: $versionName
                            """.trimIndent()
                        )
                        logger.lifecycle("--------------------------------------")
                    }
                }
            }

            // ---------- Signing ------------------------------------------------------------------
            extensions.configure<SigningExtension> {

                val filePath = findLocalProperty("signing.filePath")
                val password = findLocalProperty("signing.password")

                if (filePath.isBlank() || password.isBlank()) {
                    throw IllegalArgumentException("Signing properties are missing.")
                }

                val signingFile = file(filePath)

                if (!signingFile.exists()) {
                    throw IllegalArgumentException("Signing key file not found.")
                }

                useInMemoryPgpKeys(signingFile.readText(), password)

                sign(
                    extensions
                        .getByType(PublishingExtension::class.java)
                        .publications
                        .matching {
                            it.name.startsWith("maven")
                        }
                )
            }
        }
    }

    private fun String.moduleName(): String = removePrefix("andromeda-")
        .split("-")
        .joinToString(" ") {
            when {
                it.length <= 3 -> it.uppercase()
                it.all(Char::isUpperCase) -> it
                else -> it.replaceFirstChar(Char::uppercase)
            }
        }
        .replace("Viewmodel", "ViewModel")

    private fun MavenPublication.pomOptions(module: String) {

        this.pom {

            this.name.set("Andromeda ${module.trim()}")

            this.description.set("farsroidx pre-built codes for faster and easier Android app development.")

            this.url.set("https://github.com/farsroidx/andromeda")

            this.licenses {

                this.license {

                    this.name.set("The Apache Software License, Version 2.0")

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

    private fun Project.findLocalProperty(key: String, file: String = "local.properties"): String {

        if (localProperties == null) {
            localProperties = rootDir.resolve(file)
                .takeIf { it.exists() }
                ?.inputStream()
                ?.use { Properties().apply { load(it) } }
        }

        return localProperties?.getProperty(key)
            ?: findProperty(key) as? String
            ?: ""
    }
}
