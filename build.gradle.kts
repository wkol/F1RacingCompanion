// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath (libs.android.r8)
        classpath (libs.android.gradlePluginClasspath)
        classpath (libs.kotlin.gradlePluginClasspath)
        classpath (libs.google.hilt.gradlePluginClasspath)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}