import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":common:domain"))
                implementation(Deps.Core.MVIKotlin.mvikotlin)
                implementation(Deps.Core.MVIKotlin.mvikotlinMain)
                implementation(Deps.Core.Reaktive.reaktive)
                implementation(Deps.Core.Reaktive.coroutinesInterop)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.sedsoftware.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ComposeDesktopRemindies"
            packageVersion = "1.0.0"

            modules("java.sql")

            windows {
                menuGroup = "Remindies"
                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "8A0B064A-7196-490C-9F24-1EB5CF6107EA"
            }
        }
    }
}
