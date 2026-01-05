import kotlin.text.replace

plugins {
    // github
    alias(libs.plugins.java.platform)
    // maven
    alias(libs.plugins.maven.publish)
    // buildLogic
    alias(libs.plugins.sonatype)
}

javaPlatform { allowDependencies() }

dependencies {

    constraints {

        val reason = "Andromeda modules must be consumed via the BOM to ensure version alignment."

        rootProject.subprojects
            .filter { project ->
                project.name.startsWith("lib-")
            }
            .forEach { module ->

                module.afterEvaluate {

                    val artifactId =
                        module.name.replace("lib-", "andromeda-")

                    val coordinate =
                        "${group}:$artifactId:${module.version}"

                    println("✅ BOM Bind: $coordinate")

                    api(coordinate) { because(reason) }
                }
            }
    }
}

afterEvaluate {

    publishing {

        publications {

            register<MavenPublication>("maven") {

                from(components["javaPlatform"])

                addPom(Module.BOM, group = group.toString(), version = version.toString())
            }
        }
    }
}