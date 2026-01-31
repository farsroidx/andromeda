plugins { `kotlin-dsl` }

repositories {
    google()
    mavenCentral()
}

gradlePlugin {

    plugins {

        create("andromeda-publishing") {

            id = "ir.farsroidx.andromeda-publishing"

            implementationClass = "AndromedaPublishingPlugin"

        }
    }
}

dependencies {

    compileOnly("com.android.tools.build:gradle:9.0.0")

    compileOnly(gradleApi())
}