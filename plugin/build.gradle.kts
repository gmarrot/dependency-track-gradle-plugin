plugins {
    // Project
    id("kotlin-gradle-plugin-conventions")
    id("code-coverage-conventions")
    id("dependency-versions-conventions")

    // Third-party
    alias(libs.plugins.cyclonedx.bom)
}

group = "com.betomorrow.gradle"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.bundles.feign)

    testImplementation(libs.kotlin.test.junit5)
    integrationTestImplementation(libs.kotlin.test.junit5)
}

gradlePlugin {
    val dependencyTrack by plugins.creating {
        id = "com.betomorrow.dependency-track"
        implementationClass = "com.betomorrow.gradle.dependencytrack.DependencyTrackPlugin"
    }
}
