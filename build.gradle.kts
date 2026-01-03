plugins {
    // android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

allprojects {

    group = "com.github.farsroidx"

    version = "2026.01.01"

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