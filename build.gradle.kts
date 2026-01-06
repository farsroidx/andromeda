import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    // ktLint
    alias(libs.plugins.ktlint) apply false
}

allprojects {

    group = "ir.farsroidx"

    version = "2026.01.07"

}

subprojects {

    // Android Application
    plugins.withId("com.android.application") {

        apply(plugin = "org.jlleitschuh.gradle.ktlint")

        configureKtLint(true)

    }

    // Android Library
    plugins.withId("com.android.library") {

        apply(plugin = "org.jlleitschuh.gradle.ktlint")

        configureKtLint(true)

    }

    // Kotlin-only modules
    plugins.withId("org.jetbrains.kotlin.jvm") {

        apply(plugin = "org.jlleitschuh.gradle.ktlint")

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

tasks.register("ktlintFormatAndCheck") {

    group       = "Verification"
    description = "Formats Kotlin code with ktlint and then checks style for all modules."

    val ktLintPluginId = "org.jlleitschuh.gradle.ktlint"

    val formatTasks = subprojects.mapNotNull { project ->
        if (project.plugins.hasPlugin(ktLintPluginId)) {
            project.tasks.findByName("ktlintFormat")
        } else {
            null
        }
    }

    val checkTasks = subprojects.mapNotNull { project ->
        if (project.plugins.hasPlugin(ktLintPluginId)) {
            project.tasks.findByName("ktlintCheck")
        } else {
            null
        }
    }

    dependsOn(formatTasks)

    checkTasks.forEach { checkTask ->
        checkTask.mustRunAfter(formatTasks)
    }

    dependsOn(checkTasks)
}

tasks.register("publishAndromedaToMavenLocal") {

    group       = "publishing"
    description = "Publishes all Andromeda libraries and BOM to Maven Local."

    val publishTasks = subprojects
        .filter { project ->
            project.plugins.hasPlugin("maven-publish") &&
                !project.name.contains("bom", true)
        }
        .map { project ->
            project.tasks.named("publishToMavenLocal")
        }

    dependsOn(publishTasks)

    dependsOn(":bom:publishToMavenLocal")

    finalizedBy("generateChecksums")

    doLast {
        println("Build finished with BOM version: ${rootProject.version}")
    }
}

tasks.register("generateChecksums") {

    group       = "publishing"
    description = "Generate MD5 and SHA1 checksums for all modules before publishing to Maven Local"

    mustRunAfter(tasks.withType<AbstractPublishToMaven>())

    doLast {

        val mavenLocalRepo = File(System.getProperty("user.home"), ".m2/repository")

        val groupPath = "ir/farsroidx"

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