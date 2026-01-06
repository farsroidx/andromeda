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

    version = "2026.01.05"

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

fun Project.configureKtLint(android: Boolean) {
    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
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
            project.name.startsWith("lib-")
        }
        .map { project ->
            project.tasks.named("publishToMavenLocal")
        }

    dependsOn(publishTasks)

    dependsOn(":bom:publishToMavenLocal")
}

tasks.register("publishAndromedaToMavenCentral") {

    group       = "publishing"
    description = "Publishes all Andromeda libraries and BOM to Sonatype OSSRH."

    val publishTasks = subprojects
        .filter {
            it.plugins.hasPlugin("maven-publish") &&
                !it.name.contains("bom", true)
        }
        .map { it.tasks.named("publish") }

    dependsOn(publishTasks)

    dependsOn(":bom:publish")

    doLast {
        println("Build finished with rootProject version: ${rootProject.version}")
    }
}