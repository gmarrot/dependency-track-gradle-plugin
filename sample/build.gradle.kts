plugins {
    // Project
    id("kotlin-common-conventions")
    id("code-coverage-conventions")
    id("dependency-versions-conventions")

    // Third-party
    alias(libs.plugins.cyclonedx.bom)
    id("com.betomorrow.dependency-track") version "1.0.0-SNAPSHOT"
}

group = "com.betomorrow.gradle.sample"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)

    testImplementation(libs.kotlin.test.junit5)
    integrationTestImplementation(libs.kotlin.test.junit5)
}

val cycloneDxBom = tasks.cyclonedxBom

tasks.uploadToDependencyTrack.configure {
    dependsOn(cycloneDxBom)
}

dependencyTrack {
    apiHost = "http://localhost:8081"
    apiKey = "API_KEY"

    projectName = "dependency-track-gradle-plugin-sample"

    groupName = "dependency-track"

    sbomFile = cycloneDxBom.map { task -> file("${task.destination.get()}/${task.outputName.get()}.json") }
}
