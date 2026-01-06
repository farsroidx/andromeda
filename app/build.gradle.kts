plugins {
    // android
    alias(libs.plugins.android.application)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {

    namespace = "ir.farsroidx.app"

    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        applicationId = "ir.farsroidx.andromeda"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(
        platform(
            notation = "ir.farsroidx:andromeda-bom:${rootProject.version}",
        ),
    )

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-foundation")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-foundation-ktx")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-koin")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-ktx")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-logging")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-ui")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-ui-ktx")

    // noinspection UseTomlInstead
//    implementation("ir.farsroidx:andromeda-viewmodel")
}
