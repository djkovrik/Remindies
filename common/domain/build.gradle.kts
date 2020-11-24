plugins {
    id("multiplatform-setup")
}

kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(Deps.JetBrains.DateTime.dateTime)
            }
        }
    }
}
