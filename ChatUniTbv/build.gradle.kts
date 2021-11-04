// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version = "1.5.20"
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:${kotlin_version}")
        classpath("com.google.gms:google-services:4.3.8")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}