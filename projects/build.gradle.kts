import org.jlleitschuh.gradle.ktlint.KtlintExtension
import kotlin.text.replace

plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    // ktLint
    alias(libs.plugins.ktlint) apply false
}

private val gradleLocalTaskName    = "publishToMavenLocal"
private val gradlePublishTaskName  = "publishAndromedaToMavenLocal"
private val gradleChecksumTaskName = "generateChecksums"

private val ktLintTaskName       = "ktlintFormatAndCheck"
private val ktLintFormatTaskName = "ktlintFormat"
private val ktLintCheckTaskName  = "ktlintCheck"

allprojects {

    group = "ir.farsroidx"

    version = "2026.01.08"

}

subprojects {

    // Android Library
    plugins.withId("com.android.library") {

        apply(plugin = libs.plugins.ktlint.get().pluginId)

        configureKtLint(true)

    }

    // Kotlin-only modules
    plugins.withId("org.jetbrains.kotlin.jvm") {

        apply(plugin = libs.plugins.ktlint.get().pluginId)

        configureKtLint(false)

    }
}

private fun Project.configureKtLint(android: Boolean) {
    extensions.configure<KtlintExtension> {
        this.version.set("1.8.0")
        this.android.set(android)
        this.ignoreFailures.set(false)
        this.outputColorName.set("RED")
        this.outputToConsole.set(true)
    }
}

tasks.register(ktLintTaskName) {

    group       = "Verification"
    description = "Formats Kotlin code with ktlint and then checks style for all modules."

    val ktLintPluginId = libs.plugins.ktlint.get().pluginId

    val formatTasks = mutableListOf<TaskProvider<out Task>>()
    val checkTasks  = mutableListOf<TaskProvider<out Task>>()

    subprojects.forEach { subproject ->

        subproject.pluginManager.withPlugin(ktLintPluginId) {

            formatTasks += subproject.tasks.named(ktLintFormatTaskName)
            checkTasks  += subproject.tasks.named(ktLintCheckTaskName)
        }
    }

    dependsOn(formatTasks)

    finalizedBy(checkTasks)
}

tasks.register(gradlePublishTaskName) {

    group       = "publishing"
    description = "Publishes all Andromeda libraries and BOM to Maven Local."

    val publishableProjects = subprojects.filter {
        it.plugins.hasPlugin(libs.plugins.maven.publish.get().pluginId)
    }

    val libPublishTasks = publishableProjects
        .filter { it.name != projects.bom.andromedaBom.name }
        .map { it.tasks.named(gradleLocalTaskName) }

    val bomPublishTask = publishableProjects
        .filter { it.name == projects.bom.andromedaBom.name }
        .map { it.tasks.named(gradleLocalTaskName) }
        .firstOrNull()

    dependsOn(libPublishTasks)

    dependsOn(bomPublishTask)

    bomPublishTask?.let { bomTask ->

        libPublishTasks.forEach { libTask ->

            bomTask.configure { mustRunAfter(libTask) }

        }
    }

    finalizedBy(gradleChecksumTaskName)

    doLast {
        println("✔ All libraries published, BOM published last (${rootProject.version})")
    }
}

tasks.register(gradleChecksumTaskName) {

    group       = "publishing"
    description = "Generate MD5 and SHA1 checksums for all modules before publishing to Maven Local"

    mustRunAfter(tasks.withType<AbstractPublishToMaven>())

    doLast {

        val mavenLocalRepo = File(System.getProperty("user.home"), ".m2/repository")

        val groupPath = rootProject.group.toString().replace(".", "/")

        val ourRepoDir = File(mavenLocalRepo, groupPath)

        if (ourRepoDir.exists()) {

            println("🔍 Searching for artifacts in: ${ourRepoDir.absolutePath}")

            ourRepoDir.walkTopDown()
                .filter { it.isFile }
                .forEach { file ->

                    val isArtifactFile = file.name.matches(Regex(""".*\.(aar|jar|pom|module)$"""))
                    val isChecksumFile = file.name.matches(Regex(""".*\.(md5|sha1|sha256|asc)$"""))

                    if (isArtifactFile && !isChecksumFile) {

                        val md5File = File(file.parent, "${file.name}.md5")
                        val sha1File = File(file.parent, "${file.name}.sha1")

                        if (!md5File.exists() || !sha1File.exists()) {

                            try {

                                val bytes = file.readBytes()

                                if (!md5File.exists()) {

                                    val md5 = java.security.MessageDigest.getInstance("MD5")
                                        .digest(bytes)
                                        .joinToString("") { "%02x".format(it) }

                                    md5File.writeText(md5)

                                    println("✓ Generated MD5 for: ${file.relativeTo(mavenLocalRepo)}")
                                }

                                if (!sha1File.exists()) {

                                    val sha1 = java.security.MessageDigest.getInstance("SHA-1")
                                        .digest(bytes)
                                        .joinToString("") { "%02x".format(it) }

                                    sha1File.writeText(sha1)

                                    println("✓ Generated SHA1 for: ${file.relativeTo(mavenLocalRepo)}")
                                }

                            } catch (e: java.io.IOException) {

                                println("⚠️ IO error generating checksums for ${file.name}: ${e.message}")

                            } catch (e: java.security.NoSuchAlgorithmException) {

                                println("⚠️ Algorithm not found when generating checksums for ${file.name}: ${e.message}")

                            }
                        }
                    }
                }
        }
    }
}