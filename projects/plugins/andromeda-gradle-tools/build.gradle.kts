plugins {
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.jvm)
    // gradle
    alias(libs.plugins.gradle.publish)
    // buildLogic
    alias(libs.plugins.andromeda.publishing)
}

version = "1.0.0"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(
        kotlin("gradle-plugin"),
    )
    compileOnly(libs.android.build.gradle)
}

gradlePlugin {

    website.set("https://github.com/farsroidx/andromeda")
    vcsUrl.set("https://github.com/farsroidx/andromeda")

    plugins {

        create("andromeda-gradle-tools") {
            id = "${rootProject.group}.andromeda-gradle-tools"
            implementationClass = "ir.farsroidx.AndromedaToolsPlugin"
            displayName = "Andromeda Gradle Plugin"
            description =
                """
                Andromeda Gradle Plugin by Farsroidx provides a set of pre-configured extensions, tasks, and utility functions 
                to streamline project setup and build automation. It automatically applies essential pre-build configurations, 
                adds reusable Kotlin DSL extensions, and simplifies dependency management for Android and JVM projects. 
                This plugin is designed to save development time by providing ready-to-use pre-build code, custom tasks, 
                and build conventions that can be immediately applied to any project.
                """.trimIndent()
            tags.set(
                listOf(
                    "andromeda",
                    "pre-built",
                    "kotlin",
                    "utilities",
                    "tools",
                ),
            )
        }
    }
}

tasks.named<Jar>("jar") {

    manifest {

        attributes(
            "Andromeda-Name" to project.name,
            "Andromeda-Group" to project.group,
            "Andromeda-Version" to project.version,
            "Andromeda-Release-Date" to System.currentTimeMillis(),
        )
    }
}

tasks.withType<PublishToMavenLocal>().configureEach {
    dependsOn(tasks.withType<Sign>())
}
