plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

allprojects {

    group = "ir.farsroidx"

    version = "2026.01.05"

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