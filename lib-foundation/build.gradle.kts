plugins {
    // android
    alias(libs.plugins.android.library)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android)
    // maven
    alias(libs.plugins.maven.publish)
}

android {

    namespace  = "ir.farsroidx.andromeda.foundation"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk                    = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes { release { isMinifyEnabled = false } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {

        jvmToolchain(17)

        compilerOptions {

            freeCompilerArgs.addAll(
                "-Xcontext-parameters", "-Xannotation-default-target=param-property"
            )
        }
    }

    publishing {

        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}

afterEvaluate {

    publishing {

        publications {

            register<MavenPublication>("release") {

                groupId    = "com.github.farsroidx"
                artifactId = "andromeda-foundation"
                version    = "1.0.0"

                from( components["release"] )
            }
        }
    }
}