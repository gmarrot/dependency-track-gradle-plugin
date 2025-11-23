plugins {
    // Project
    id("kotlin-common-conventions") apply false
    id("code-coverage-conventions") apply false
    id("dependency-versions-conventions") apply false

    // Third-party
    alias(libs.plugins.cyclonedx.bom) apply false
}
