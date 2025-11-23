plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.ktlint.gradle)
    implementation(libs.dependency.versions.gradle)
}
