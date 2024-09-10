pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // Required for Firebase and Android libraries
        mavenCentral()
    }
}

rootProject.name = "Android-Test-01"
include(":app")
