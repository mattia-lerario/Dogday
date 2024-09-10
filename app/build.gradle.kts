// Module-level build.gradle.kts (e.g., app/build.gradle.kts)
plugins {
    alias(libs.plugins.android.application)  // Applying the android application plugin
    alias(libs.plugins.jetbrains.kotlin.android)  // Applying the Kotlin plugin for Android
    alias(libs.plugins.google.services)  // Applying Google services plugin for Firebase
}

android {
    namespace = "com.example.myapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)
    testImplementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(platform(libs.firebase.bom))  // Firebase BoM

    // Firebase services
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.google.firebase.database.ktx)

    // Firebase UI
    implementation(libs.firebase.ui.auth)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
