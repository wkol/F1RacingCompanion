plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

android {

    defaultConfig {
        applicationId = "com.example.f1racingcompanion"
        minSdk = 24
        targetSdk = 33
        compileSdk = 32
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        kapt.includeCompileClasspath = false
    }

    packagingOptions {
        resources {
            pickFirsts += setOf("META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        @Suppress("SuspiciousCollectionReassignment")
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        )
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
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.immutable.collections)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material.material)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.animation)
    implementation(libs.compose.navigation)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
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
    implementation(libs.scarlet.protocol.websocket)
    implementation(libs.scarlet.message.adapter.moshi)
    implementation(libs.scarlet.lifecycle.android)
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
