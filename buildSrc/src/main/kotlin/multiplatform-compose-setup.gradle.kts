import org.jetbrains.compose.compose

plugins {
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
        named("androidMain") {
            dependencies {
                api(Deps.Android.JetPack.appCompat)
                api(Deps.Android.JetPack.ktx)
            }
        }
    }
}
