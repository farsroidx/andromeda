plugins {
    // android
    alias(libs.plugins.android.library)
    // maven
    alias(libs.plugins.maven.publish)
    // buildLogic
    alias(libs.plugins.andromeda.publishing)
}

version = "3.0.0"

android {

    namespace = "andromeda.ui.ktx"

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

    publishing {

        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {

    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xannotation-default-target=param-property",
            "-Xexplicit-backing-fields",
        )
    }
}

dependencies {

    // Andromeda
    api(projects.android.andromedaUi)

    // Androidx
    api(libs.androidx.recyclerview)
    api(libs.androidx.annotation.jvm)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
