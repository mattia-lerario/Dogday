plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")


}

android {
    namespace = "com.example.dogday"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dogday"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Use the correct version here based on your dependencies
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Firebase Platform BOM for managing versions
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

    // Firebase dependencies
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.1.0")
    implementation("io.coil-kt:coil:2.4.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose) // Add this line for navigation in Compose
    implementation(libs.androidx.ui) // Corrected from libs.androidx.ui
    implementation(libs.androidx.ui.graphics) // Corrected from libs.androidx.ui.graphics
    implementation(libs.androidx.ui.tooling.preview) // Corrected from libs.androidx.ui.tooling.preview
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation("com.google.maps.android:maps-compose:6.1.2")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation(libs.volley)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4) // Corrected from libs.androidx.ui.test.junit4
    debugImplementation(libs.androidx.ui.tooling) // Corrected from libs.androidx.ui.tooling
    debugImplementation(libs.androidx.ui.test.manifest) // Corrected from libs.androidx.ui.test.manifest

}
