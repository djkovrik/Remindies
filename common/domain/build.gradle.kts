plugins {
    id("multiplatform-setup")
    id("android-setup")
    id("multiplatform-compose-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.Core.MVIKotlin.rx)
                implementation(Deps.Core.MVIKotlin.mvikotlin)
                implementation(Deps.Core.MVIKotlin.mvikotlinExtensionsReaktive)
                implementation(Deps.Core.Decompose.decompose)
                implementation(Deps.Core.Decompose.extensionsCompose)
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
