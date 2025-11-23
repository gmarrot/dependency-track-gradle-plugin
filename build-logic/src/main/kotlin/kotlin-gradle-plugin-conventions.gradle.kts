plugins {
    // Core
    `java-gradle-plugin`
    `maven-publish`

    // Project
    id("kotlin-common-conventions")
}

java {
    withSourcesJar()
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class)
        gradlePlugin.testSourceSets.add(integrationTest.sources)
    }
}
