import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android)       apply false
    alias(libs.plugins.jetbrains.kotlin.compose)       apply false
    alias(libs.plugins.jetbrains.kotlin.parcelize)     apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    // ktLint
    alias(libs.plugins.ktlint) apply false
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