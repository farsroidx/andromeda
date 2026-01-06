plugins {
    // maven
    id("maven-publish")
    // security
    id("signing")
}

afterEvaluate {

    publishing {

        repositories {

            maven {

                this.name = "Sonatype"

                this.url = if (isSnapshotBuild()) {
                    uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }

                val username = findLocalProperty(key = "sonatype.username")
                val password = findLocalProperty(key = "sonatype.password")

                if (username.isBlank() || password.isBlank()) {
                    throw IllegalArgumentException("sonatype.username or sonatype.password is missing from local.properties.")
                }

                this.credentials {
                    this.username = findLocalProperty(key = "sonatype.username")
                    this.password = findLocalProperty(key = "sonatype.password")
                }
            }
        }
    }

    signing {

        val filePath = findLocalProperty(key = "signing.filePath")
        val password = findLocalProperty(key = "signing.password")

        if (filePath.isBlank()) {
            throw IllegalArgumentException("signing.filePath is missing from local.properties.")
        }

        val signingFile = project.file(filePath)

        if (!signingFile.exists() || password.isBlank()) {
            throw IllegalArgumentException("signing.filePath or signing.password is missing from local.properties.")
        }

        this.useInMemoryPgpKeys(signingFile.readText(), password)

        this.sign(publishing.publications)
    }
}