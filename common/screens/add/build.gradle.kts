plugins {
    id("multiplatform-setup")
    id("android-setup")
    id("multiplatform-compose-setup")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":common:tools"))
                implementation(project(":common:database"))
                implementation(project(":common:domain"))

                implementation(Deps.Core.MVIKotlin.mvikotlin)
                implementation(Deps.Core.MVIKotlin.mvikotlinExtensionsReaktive)
                implementation(Deps.Core.Reaktive.reaktive)
            }
        }
    }
}
