@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
}

kotlin {
    android()

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Deps.Core.Reaktive.reaktive)
                implementation(Deps.JetBrains.DateTime.dateTime)
            }
        }

        named("commonTest") {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testCommon)
                implementation(Deps.JetBrains.Kotlin.testAnnotationsCommon)
            }
        }

        named("androidTest") {
            dependencies {
                implementation(Deps.JetBrains.Kotlin.testJunit)
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}
