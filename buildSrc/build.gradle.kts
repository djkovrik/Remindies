plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    // TODO: remove after new build is published
    mavenLocal()
    google()
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

buildscript {
    repositories {
        // TODO: remove after new build is published
        mavenLocal()
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath(Deps.JetBrains.Compose.gradlePlugin)
        classpath(Deps.JetBrains.Kotlin.gradlePlugin)
        classpath(Deps.Android.Build.gradlePlugin)
        classpath(Deps.Tools.Detekt.detekt)
    }
}

dependencies {
    implementation(Deps.JetBrains.Compose.gradlePlugin)
    implementation(Deps.JetBrains.Kotlin.gradlePlugin)
    implementation(Deps.Android.Build.gradlePlugin)
}

kotlin {
    // Add Deps to compilation, so it will become available in main project
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}