// Project-level build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
}

// No need to apply plugins here, they will be applied in the module-level build.gradle.kts
