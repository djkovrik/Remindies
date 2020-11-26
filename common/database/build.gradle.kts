plugins {
    id("multiplatform-setup")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("RemindieDatabase") {
        packageName = "com.sedsoftware.common.database"
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.Core.Reaktive.reaktive)
            }
        }

        androidMain {
            dependencies {
                implementation(Deps.Core.SQLDelight.androidDriver)
                implementation(Deps.Core.SQLDelight.sqliteDriver)
            }
        }

        desktopMain {
            dependencies {
                implementation(Deps.Core.SQLDelight.sqliteDriver)
            }
        }
    }
}
