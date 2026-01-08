plugins {
    // android
    alias(libs.plugins.android.library)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.jetbrains.kotlin.android)
    // maven
    alias(libs.plugins.maven.publish)
    // builLogic
    alias(libs.plugins.andromeda.publishing)
}

version = "1.0.0"

android {

    namespace = "ir.farsroidx.andromeda.ui"

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

    buildFeatures {
        dataBinding = true
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

    // Androidx
    api(libs.androidx.core)
    api(libs.androidx.activity)
    api(libs.androidx.fragment)
    api(libs.androidx.appcompat)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
