plugins {
    `kotlin-dsl`
    id("io.gitlab.arturbosch.detekt").version("1.14.2")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://kotlin.bintray.com/kotlinx/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}


detekt {
    input = files("$projectDir/android/", "$projectDir/common/", "$projectDir/desktop/")
    config = files("$projectDir/detekt/base-config.yml")
    baseline = file("$projectDir/detekt/baseline.xml")
    parallel = true

    reports {
        html {
            enabled = true
            destination = file("$projectDir/detekt/reports/detekt.html")
        }
    }
}

tasks.register("runOnGitHub") {
    dependsOn(":detekt", ":android:lint", ":common:domain:testDebugUnitTest")
    group = "custom"
    description = "./gradlew runOnGitHub # runs on GitHub Action"
}
