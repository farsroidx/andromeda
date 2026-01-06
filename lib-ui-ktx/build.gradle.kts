plugins {
    // android
    alias(libs.plugins.android.library)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.android)
    // maven
    alias(libs.plugins.maven.publish)
    // buildLogic
    alias(libs.plugins.sonatype)
}

version = "1.0.0"

android {

    namespace = "ir.farsroidx.andromeda.ui.ktx"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 23
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
                "-Xcontext-parameters",
                "-Xannotation-default-target=param-property",
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

    // Andromeda
    api(projects.libUi)

    // Androidx
    api(libs.androidx.recyclerview)
    api(libs.androidx.annotation.jvm)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {

    publishing {

        publications {

            register<MavenPublication>("release") {

                from(components["release"])

                pomOptions(Module.UI_KTX, group.toString(), version.toString())
            }
        }
    }
}
