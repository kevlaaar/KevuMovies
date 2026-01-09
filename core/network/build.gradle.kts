import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kevlaaar.kevumovies.core.network"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // TODO: TMDB API configuration
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
        buildConfigField("String", "TMDB_IMAGE_URL", "\"https://image.tmdb.org/t/p/\"")

        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if(localPropertiesFile.exists()){
            properties.load(localPropertiesFile.inputStream())
        }
        val tmdbApiToken = properties.getProperty("TMDB_API_TOKEN") ?: ""
        buildConfigField("String", "TMDB_API_TOKEN", "\"$tmdbApiToken\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.coroutines)

    // Network
    implementation(libs.bundles.network)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Testing
    testImplementation(libs.bundles.testing)
}