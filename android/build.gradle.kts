import org.jetbrains.compose.compose

plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    kotlin("android")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1000
        versionName = "0.0.1"
        applicationId = "com.sedsoftware.remindies"
        resConfigs("en", "ru")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/*")
    }
}

dependencies {
    implementation(project(":common:domain"))
    implementation(compose.ui)
    implementation(compose.material)

    implementation(Deps.Core.MVIKotlin.mvikotlin)
    implementation(Deps.Core.MVIKotlin.mvikotlinMain)
    implementation(Deps.Core.MVIKotlin.mvikotlinLogging)
    implementation(Deps.Core.MVIKotlin.mvikotlinTimeTravel)
}
