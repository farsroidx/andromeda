plugins {
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.jvm)
    // gradle
    alias(libs.plugins.gradle.publish)
    // buildLogic
    alias(libs.plugins.andromeda.publishing)
}

version = "1.1.0"

gradlePlugin {

    plugins {

        create("andromeda-tools") {

            id = "${rootProject.group}.andromeda-tools"

            implementationClass = "ir.farsroidx.AndromedaToolsPlugin"

        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
//    implementation(
//        kotlin("gradle-plugin", version = "2.2.21")
//    )
    compileOnly(kotlin("gradle-plugin"))
    compileOnly(libs.android.build.gradle)
}

gradlePlugin {

    website.set("https://github.com/farsroidx/andromeda-gradle-plugin")
    vcsUrl.set("https://github.com/farsroidx/andromeda-gradle-plugin")

    plugins {

        create("andromeda") {
            id                  = "ir.farsroidx.andromeda"
            implementationClass = "ir.farsroidx.plugin.andromeda.AndromedaPlugin"
            displayName         = "Andromeda Gradle Plugin"
            description         = """
            Andromeda Gradle Plugin by Farsroidx provides a set of pre-configured extensions, tasks, and utility functions 
            to streamline project setup and build automation. It automatically applies essential pre-build configurations, 
            adds reusable Kotlin DSL extensions, and simplifies dependency management for Android and JVM projects. 
            This plugin is designed to save development time by providing ready-to-use pre-build code, custom tasks, 
            and build conventions that can be immediately applied to any project.
            """.trimIndent()
            tags.set(
                listOf(
                    "andromeda", "pre-built", "kotlin", "utilities"
                )
            )
        }
    }
}

tasks.register("GenerateVersionFile") {

    val packageName = project.group
    val packagePath = packageName.toString().replace(".", "/")

    val file = file("src/main/kotlin/$packagePath/Version.kt")

    file.writeText(
        """
        package $packageName
        
        object Version {
            const val PLUGIN_VERSION   = "${project.version}"
            const val RELEASE_DATETIME = ${System.currentTimeMillis()}
        }
        """.trimIndent()
    )
}