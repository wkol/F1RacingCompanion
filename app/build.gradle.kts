



plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.f1racingcompanion"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        kapt.includeCompileClasspath = false
    }

    packagingOptions.resources.excludes += setOf("META-INF/*", "META-INF/gradle/*")

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        @Suppress("SuspiciousCollectionReassignment")
        freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(libs.google.hilt.library)
    implementation(libs.google.hilt.compiler)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.kotlin.reflect)
    implementation(libs.compose.ui)
    implementation(libs.compose.material.material)
    implementation(libs.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlin.coroutines.android)
    coreLibraryDesugaring(libs.android.desugar)
    // Dagger(Hilt) packages
    kapt(libs.google.hilt.compiler)
    testImplementation(libs.google.hilt.testing)
    testAnnotationProcessor(libs.google.hilt.compiler)
    // Retrofit packages
    implementation(libs.retrofit)
    implementation(libs.moshi)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit.converter.moshi)

    // Scarlet package
    implementation(libs.scarlet)
    implementation(libs.scarlet.lifecycle.android)
    implementation(libs.scarlet.protocol.websocket)
    implementation(libs.scarlet.message.adapter.moshi)
    // Timber log package
    implementation(libs.timber)

    // Moshi wrapped annotation
    implementation(libs.moshi.lazy)
    implementation(libs.compose.material.iconsext)
    // Test packages
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.okhttp.mockwebserver.extension)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
}
