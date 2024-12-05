plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "au.com.woolworths.store.callbell"
    compileSdk = 34

    defaultConfig {
        applicationId = "au.com.woolworths.store.callbell"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        buildConfigField ("String", "BASE_URL", "\"https://storesinframobilitymessagesprod.azurewebsites.net\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        setProperty("archivesBaseName", "ww-smart-button-$versionName")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
    flavorDimensions += "version"
    productFlavors {
        create("uat") {
            dimension = "version"
            applicationIdSuffix = ".uat"
            buildConfigField ("String", "BASE_URL", "\"https://storesinframobilitypttuat.azurewebsites.net/\"")
        }
        create("prod") {
            dimension = "version"
            applicationIdSuffix = ".prod"
            buildConfigField ("String", "BASE_URL", "\"https://storesinframobilitymessagesprod.azurewebsites.net\"")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation(files("libs/FeatureModule-android-2.0.1.aar"))
    implementation(files("libs/AirWatchSDK-21.5.1.aar"))
    implementation(files("libs/sdk-fm-extension-android-1.2.aar"))
    implementation(files("libs/WorkspaceOneClient-release-22.4.0.0.aar"))
    implementation(files("libs/ws1-android-logger-1.2.0.aar"))
}