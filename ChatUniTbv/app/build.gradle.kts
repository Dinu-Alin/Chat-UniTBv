plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(31)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.lagar.chatunitbv"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("com.google.firebase:firebase-messaging:23.0.0")
    implementation("androidx.work:work-runtime:2.7.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("io.grpc:grpc-okhttp:1.32.2")

    val kotlin_version = "1.5.20"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlin_version}")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.github.rosariopfernandes:firecoil:0.3.0")


    //Volley
    //implementation("com.android.volley:volley:1.2.0")
//
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.0")

    // GSON
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")

    // Coil
    implementation("io.coil-kt:coil:1.2.2")

    // Material Design
    implementation("com.google.android.material:material:1.4.0")

    // Timber
    implementation("com.github.ajalt:timberkt:1.5.1")

    // Room
//    val room_version = "2.3.0"
//    implementation("androidx.room:room-runtime:${room_version}")
//    annotationProcessor("androidx.room:room-compiler:${room_version}")

    // FastAdapter

    val latestFastAdapterRelease = "5.6.0"
    implementation("com.mikepenz:fastadapter:${latestFastAdapterRelease}")
    implementation("com.mikepenz:fastadapter-extensions-expandable:${latestFastAdapterRelease}")
    implementation("com.mikepenz:fastadapter-extensions-binding:${latestFastAdapterRelease}") // view binding helpers
    implementation("com.mikepenz:fastadapter-extensions-diff:${latestFastAdapterRelease}") // diff util helpers
    implementation("com.mikepenz:fastadapter-extensions-drag:${latestFastAdapterRelease}") // drag support
    implementation("com.mikepenz:fastadapter-extensions-paged:${latestFastAdapterRelease}") // paging support
    implementation("com.mikepenz:fastadapter-extensions-scroll:${latestFastAdapterRelease}")// scroll helpers
    implementation("com.mikepenz:fastadapter-extensions-swipe:${latestFastAdapterRelease}")// swipe support
    implementation("com.mikepenz:fastadapter-extensions-ui:${latestFastAdapterRelease}")// pre-defined ui components
    implementation("com.mikepenz:fastadapter-extensions-utils:${latestFastAdapterRelease}")// needs the `expandable`, `drag` and `scroll` extension.


}