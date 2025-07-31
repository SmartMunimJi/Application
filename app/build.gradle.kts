plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.project.smartmunimji"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project.smartmunimji"
        minSdk = 30
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Existing AndroidX dependencies
    implementation("androidx.core:core-ktx:1.13.0") // Updated in your context
    implementation("androidx.appcompat:appcompat:1.7.0") // Updated in your context
    implementation("com.google.android.material:material:1.12.0") // Updated in your context
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2") // From your previous provided list
    implementation("androidx.fragment:fragment-ktx:1.6.2") // From your previous provided list
    implementation("androidx.activity:activity-ktx:1.10.1") // Updated in your context
    implementation("androidx.activity:activity:1.10.1") // Updated in your context

    // --- New API Integration Dependencies ---
    // Networking: Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // For converting JSON to/from Java/Kotlin objects
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // Updated to latest stable for bug fixes
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // For logging network requests/responses (helpful for debugging)

    // Kotlin Coroutines for asynchronous operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // Lifecycle Extensions for ViewModelScope and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2") // For ViewModelScope
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")   // For LiveData

    // For secure SharedPreferences (optional but recommended for JWT)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1") // Corrected to match your context
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1") // Corrected to match your context
}