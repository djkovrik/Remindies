plugins {
    id("multiplatform-setup")
    id("android-setup")
    id("multiplatform-compose-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.Core.MVIKotlin.mvikotlin)
                implementation(Deps.Core.MVIKotlin.mvikotlinExtensionsReaktive)
                implementation(Deps.Core.Reaktive.reaktive)
            }
        }

        commonTest {
            dependencies {
                implementation(Deps.JetBrains.DateTime.dateTime)
            }
        }
    }
}
