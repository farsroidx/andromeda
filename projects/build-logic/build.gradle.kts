plugins { `kotlin-dsl` }

gradlePlugin {

    plugins {

        create("andromeda-publishing") {

            id = "ir.farsroidx.andromeda-publishing"

            implementationClass = "AndromedaPublishingPlugin"

        }
    }
}

dependencies {

    //noinspection UseTomlInstead
    compileOnly("com.android.tools.build:gradle:9.0.1")

    compileOnly(gradleApi())
}