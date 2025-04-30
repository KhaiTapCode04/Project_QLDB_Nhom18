pluginManagement {
    plugins {
        id("com.android.application") version "8.8.0"
        id("org.jetbrains.kotlin.android") version "1.9.24"
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Nhom18_LTTBDD_QLDB_NgayBC"
include(":app")
