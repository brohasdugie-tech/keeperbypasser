plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.example.devicemanager"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.devicemanager"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
}
