plugins {
    // android
    alias(libs.plugins.android.library)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.compose)
    // maven
    alias(libs.plugins.maven.publish)
    // buildLogic
    alias(libs.plugins.andromeda.publishing)
}

version = "1.0.0"

android {

    namespace = "andromeda.ui.compose"

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
        compose = true
    }

    publishing {

        singleVariant("release") {
            withSourcesJar()
            // withJavadocJar()
        }
    }
}

kotlin {

    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xannotation-default-target=param-property",
            "-Xexplicit-backing-fields"
        )
    }
}

dependencies {

    // Andromeda
    api(projects.android.andromedaUiCore)

    // Compose
    implementation( platform( libs.compose.bom ) )
    implementation(libs.bundles.compose)
    debugApi(libs.compose.ui.tooling)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
