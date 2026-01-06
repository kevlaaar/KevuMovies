pluginManagement {
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

rootProject.name = "KevuMovies"
include(":app")
include(":core")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:ui")
include(":core:domain")
include(":feature:home")
include(":feature:search")
include(":feature:details")
include(":feature:favorites")
