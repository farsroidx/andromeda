plugins {
    // maven
    id("maven-publish")
    // security
    id("signing")
}

afterEvaluate {

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