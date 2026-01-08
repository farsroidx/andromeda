plugins {
    // github
    alias(libs.plugins.java.platform)
    // maven
    alias(libs.plugins.maven.publish)
    // buildLogic
    alias(libs.plugins.andromeda.publishing)
}

javaPlatform { allowDependencies() }

dependencies {

    constraints {

        val reason = "Andromeda modules must be consumed via the BOM to ensure version alignment."

        println()
        println("============================================================")
        println("=--------------------- BOM MODULE -------------------------=")
        println("============================================================")

        listOf(
            projects.android.andromedaFoundation,
            projects.android.andromedaFoundationKtx,
            projects.android.andromedaKtx,
            projects.android.andromedaLogging,
            projects.android.andromedaUi,
            projects.android.andromedaUiKtx,
            projects.android.andromedaViewmodel
        ).forEach { project ->

            api(project) { because(reason) }

            println("✓ Module: ${project.name}:${project.version}")

        }

        println("============================================================")
        println("=--------------------- BOM MODULE -------------------------=")
        println("============================================================")
        println()
    }
}