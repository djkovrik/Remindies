object Deps {

    object JetBrains {
        object Kotlin {
            private const val VERSION = "1.4.32"
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
            const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:$VERSION"
            const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
            const val testAnnotationsCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common:$VERSION"
        }

        object Compose {
            private const val VERSION = "0.4.0-build183"
            const val gradlePlugin = "org.jetbrains.compose:compose-gradle-plugin:$VERSION"
        }

        object DateTime {
            private const val VERSION = "0.2.1"
            const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$VERSION"
        }
    }

    object Android {
        object Build {
            const val gradlePlugin = "com.android.tools.build:gradle:4.0.1"
        }
        object JetPack {
            const val appCompat = "androidx.appcompat:appcompat:1.3.0-beta01"
            const val ktx = "androidx.core:core-ktx:1.3.2"
        }
        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0-alpha02"
        }
    }

    object Tools {
        object Detekt {
            const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2"
        }
    }

    object Core {
        object MVIKotlin {
            private const val VERSION = "2.0.2"
            const val rx = "com.arkivanov.mvikotlin:rx:$VERSION"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mvikotlinTimeTravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:$VERSION"
            const val mvikotlinExtensionsReaktive = "com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:$VERSION"
        }

        object Decompose {
            private const val VERSION = "0.2.3"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val extensionsCompose = "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
        }

        object Reaktive {
            private const val VERSION = "1.1.22"
            const val reaktive = "com.badoo.reaktive:reaktive:$VERSION"
            const val reaktiveTesting = "com.badoo.reaktive:reaktive-testing:$VERSION"
            const val utils = "com.badoo.reaktive:utils:$VERSION"
            const val coroutinesInterop = "com.badoo.reaktive:coroutines-interop:$VERSION"
        }

        object SQLDelight {
            private const val VERSION = "1.4.4"

            const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
            const val androidDriver = "com.squareup.sqldelight:android-driver:$VERSION"
            const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:$VERSION"
        }
    }
}
