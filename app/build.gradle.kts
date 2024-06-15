plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.maps.secrets)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "it.unito.progmob"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.unito.progmob"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "it.unito.progmob.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
    secrets {
        propertiesFileName = "secrets.properties"
        defaultPropertiesFileName = "local.defaults.properties"
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
    }
}

dependencies {
    // [MODULE DEPENDENCIES]
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // Kotlin
    implementation(platform(libs.kotlin.bom))
    // Extended Material Icons
    implementation(libs.androidx.material.icons.extended)
    // Lifecycle & ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Navigation
    implementation(libs.androidx.navigation.compose)
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    // Dagger-Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.dagger.hilt.compiler)
    // Maps
    implementation(libs.google.maps.compose)
    implementation(libs.google.maps.compose.utils)
    implementation(libs.google.maps.compose.widgets)
    implementation(libs.google.play.services.maps)
    implementation(libs.google.play.services.location)
    // Vico Charts
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.core)

    // [UNIT TESTING DEPENDENCIES]
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.rules)

    // [INSTRUMENTATION ANDROID TESTING DEPENDENCIES]
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.core.ktx)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // [DEBUG BUILD DEPENDENCIES]
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}