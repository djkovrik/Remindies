plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
}

kotlin {
    jvm("desktop")
    android()

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(Deps.JetBrains.DateTime.dateTime)
                implementation(Deps.Core.Reaktive.reaktive)
                implementation(Deps.Core.MVIKotlin.mvikotlin)
                implementation(Deps.Core.MVIKotlin.mvikotlinExtensionsReaktive)
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
        named("desktopTest") {
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
        minSdkVersion(23)
        targetSdkVersion(30)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}
